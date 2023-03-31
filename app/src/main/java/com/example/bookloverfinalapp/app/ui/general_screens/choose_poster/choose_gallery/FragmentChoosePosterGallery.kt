package com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.choose_gallery

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.example.bookloverfinalapp.app.base.BaseBindingFragment
import com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.PhotoType
import com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.PosterItem
import com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.choose_online.fingerprint.PosterFingerprint
import com.joseph.ui_core.adapter.FingerprintAdapter
import com.example.bookloverfinalapp.app.utils.extensions.swapElements
import com.example.bookloverfinalapp.databinding.FragmentChoosePosterGalleryBinding
import com.joseph.ui_core.custom.modal_page.dismissModalPage
import com.joseph.ui_core.extensions.launchOnViewLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


class FragmentChoosePosterGallery :
    BaseBindingFragment<FragmentChoosePosterGalleryBinding>(FragmentChoosePosterGalleryBinding::inflate) {

    private val adapter = FingerprintAdapter(
        listOf(PosterFingerprint())
    )

    private var onLocalPosterSelectedListener: ((String) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        launchOnViewLifecycle {
            val photos = getAllPhotosFromExternalStorage()
            adapter.submitList(photos.swapElements())
        }
    }

    private fun setupViews() = with(binding()) {
        recyclerView.adapter = adapter
    }

    private fun mapUriToPosterItem(fileUri: String) = PosterItem(
        type = PhotoType.LOCAL,
        posterFileUri = fileUri,
        onClickListener = { uri ->
            onLocalPosterSelectedListener?.invoke(uri)
            dismissModalPage()
        }
    )

    private suspend fun getAllPhotosFromExternalStorage(): List<PosterItem> {
        var photo: String
        val photoList: ArrayList<String> = ArrayList()

        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA
        )

        val images: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val cur: Cursor = withContext(Dispatchers.IO) {
            return@withContext requireActivity().contentResolver.query(
                images, projection,
                null,
                null,
                null
            )
        } ?: return emptyList()

        withContext(Dispatchers.IO) {
            return@withContext async {
                if (cur.moveToFirst()) {
                    var dataUri: String
                    val dataColumn: Int = cur.getColumnIndex(MediaStore.Images.Media.DATA)
                    do {
                        dataUri = cur.getString(dataColumn)
                        photo = dataUri
                        photoList.add(photo)
                    } while (cur.moveToNext())
                }
            }
        }.await()
        cur.close()
        return photoList.map(::mapUriToPosterItem)
    }


    companion object {

        fun newInstance(onLocalPosterSelectedListener: (String) -> Unit) =
            FragmentChoosePosterGallery().apply {
                this.onLocalPosterSelectedListener = onLocalPosterSelectedListener
            }
    }
}