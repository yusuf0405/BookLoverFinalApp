package com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.AddNewBook
import com.example.bookloverfinalapp.app.utils.cons.PERMISSION_CODE
import com.example.bookloverfinalapp.app.utils.cons.READ_EXTERNAL_STORAGE
import com.example.bookloverfinalapp.app.utils.cons.REQUEST_CODE
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.FragmentAdminUploadPdfBinding
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.parse.ParseFile
import com.parse.SaveCallback
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


@Suppress("DEPRECATION")
@AndroidEntryPoint
class FragmentAdminUploadPdf :
    BaseFragment<FragmentAdminUploadPdfBinding, FragmentAdminUploadPdfViewModel>(
        FragmentAdminUploadPdfBinding::inflate), OnPageChangeListener, OnLoadCompleteListener,
    OnErrorListener {

    override val viewModel: FragmentAdminUploadPdfViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private var bookFile: ParseFile? = null
    private var bookChapterCount: Int = 0
    private var bookPageCount: Int = 0
    private var bookPoster: ParseFile? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding().savePdfButton.setOnClickListener {
            saveFiles()
        }
        binding().pickPdfButton.setOnClickListener {
            pickFile()
        }
        binding().toChange.setOnClickListener {
            getImage()
        }
    }

    private fun pickFile() {
        val permissionCheck = ContextCompat.checkSelfPermission(requireContext(),
            READ_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(READ_EXTERNAL_STORAGE),
                PERMISSION_CODE)
            return
        }
        launchPicker()
    }

    private fun launchPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
            flags = flags or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun displayFromUri(uri: Uri) {
        binding().pdfViewAdmin.fromUri(uri)
            .defaultPage(0)
            .onPageChange(this)
            .enableAnnotationRendering(true)
            .onLoad(this)
            .scrollHandle(DefaultScrollHandle(requireContext()))
            .onError(this)
            .load()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null && data.data != null) {
            if (requestCode == REQUEST_CODE) {
                val uri = data.data!!
                requireActivity().contentResolver?.takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION)
                displayFromUri(uri)
                generateImageFromPdf(uri)
                bookFile = ParseFile(getFile(uri))
            } else {
                val uri = data.data!!
                bookPoster = ParseFile("image.png", uriToImage(uri))
                requireContext().glide(uri, binding().roundedBookImage)
            }

        }
    }

    private fun saveFiles() {
        when {
            binding().editTextBookTitle.text.isBlank() -> showToast(R.string.fill_in_all_fields)
            binding().editTextBookAutor.text.isBlank() -> showToast(R.string.fill_in_all_fields)
            binding().editTextBookPublicYear.text.isBlank() -> showToast(R.string.fill_in_all_fields)
            else -> {
                loadingDialog.show()
                if (bookFile != null) {
                    bookFile!!.saveInBackground(SaveCallback { it ->
                        if (it == null) {
                            bookPoster!!.saveInBackground(SaveCallback {
                                if (it == null) {
                                    addNewBook()
                                }
                            })
                        }
                    })
                }
            }
        }
    }

    private fun addNewBook() {
        binding().apply {
            val newBook = AddNewBook(author = editTextBookAutor.text.toString(),
                chapterCount = bookChapterCount,
                title = binding().editTextBookTitle.text.toString(),
                publicYear = binding().editTextBookPublicYear.text.toString(),
                poster = bookPoster!!.toBookImage(),
                book = bookFile!!.toPdf(), page = bookPageCount,
                schoolId = currentUser.schoolId)

            viewModel.addBook(book = newBook).observe(viewLifecycleOwner) {
                showToast(R.string.book_added_successfully)
            }
        }

    }


    override fun onPageChanged(page: Int, pageCount: Int) {
        bookPageCount = pageCount
    }


    private fun generateImageFromPdf(pdfUri: Uri) = lifecycleScope.launch(Dispatchers.IO) {
        val pageNumber = 0
        val pdfiumCore = PdfiumCore(requireContext())
        try {
            val fd: ParcelFileDescriptor =
                requireActivity().contentResolver.openFileDescriptor(pdfUri, "r")!!
            val pdfDocument = pdfiumCore.newDocument(fd)
            pdfiumCore.openPage(pdfDocument, pageNumber)
            val width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber)
            val height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height)
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()
            bookPoster = ParseFile("image.png", byteArray)
            pdfiumCore.closeDocument(pdfDocument)
            withContext(Dispatchers.Main) {
                requireContext().glide(bmp,
                    binding().roundedBookImage)
            }
        } catch (e: Exception) {
            //todo with exception
        }
    }


    override fun loadComplete(nbPages: Int) {
        binding().apply {
            savePdfButton.showView()
            pickPdfButton.hideView()
            val meta: PdfDocument.Meta = pdfViewAdmin.documentMeta
            editTextBookTitle.setText(meta.title)
            editTextBookAutor.setText(meta.author)
            bookChapterCount = pdfViewAdmin.tableOfContents.size
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        if (requestCode == PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) launchPicker()

    }

    override fun onError(t: Throwable?) {
    }

}
