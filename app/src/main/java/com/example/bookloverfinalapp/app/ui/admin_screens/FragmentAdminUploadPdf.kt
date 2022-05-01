package com.example.bookloverfinalapp.app.ui.admin_screens

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.utils.cons.FOLDER
import com.example.bookloverfinalapp.app.utils.cons.PERMISSION_CODE
import com.example.bookloverfinalapp.app.utils.cons.READ_EXTERNAL_STORAGE
import com.example.bookloverfinalapp.app.utils.cons.REQUEST_CODE
import com.example.bookloverfinalapp.databinding.FragmentAdminUploadPdfBinding
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.parse.ParseException
import com.parse.ParseFile
import com.parse.ParseObject
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import java.io.File
import java.io.FileOutputStream


@Suppress("DEPRECATION")
class FragmentAdminUploadPdf :
    BaseFragment<FragmentAdminUploadPdfBinding, FragmentAdminUploadPdfViewModel>(
        FragmentAdminUploadPdfBinding::inflate), OnPageChangeListener, OnLoadCompleteListener,
    OnErrorListener {

    override val viewModel: FragmentAdminUploadPdfViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}
    private var pageSize = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding().savePdfButton.setOnClickListener {
            pickFile()
        }
    }

    private fun pickFile() {
        val permissionCheck = ContextCompat.checkSelfPermission(requireContext(),
            READ_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(READ_EXTERNAL_STORAGE),
                PERMISSION_CODE
            )
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
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK &&
            data != null && data.data != null
        ) {
            val uri = data.data!!

            requireActivity().contentResolver?.takePersistableUriPermission(uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            displayFromUri(uri)
            val file = ParseFile(getFile(uri))
//            file.saveInBackground(SaveCallback {
//                if (it == null) {
//                    createObject(file)
//                } else {
//                    loadingDialog.dismiss()
//                    showToast(it.message!!)
//                }
//            })
            generateImageFromPdf(uri)
        }
    }

    private fun createObject(file: ParseFile) {
        val entity = ParseObject("Books")
        entity.put("title", "A string")
        entity.put("publicYear", "A string")
        entity.put("page", 1)
        entity.put("author", "A string")
        entity.put("poster", ParseFile("resume.txt", "My string content".toByteArray()))
        entity.put("chapterCount", 1)
        entity.put("book", file)

        entity.saveInBackground { e: ParseException? ->
            if (e == null) {
                Toast.makeText(requireContext(), "Savve", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFile(documentUri: Uri): File {
        val inputStream = requireActivity().contentResolver?.openInputStream(documentUri)
        var file: File
        inputStream.use { input ->
            file = File(requireActivity().cacheDir, System.currentTimeMillis().toString() + ".pdf")
            FileOutputStream(file).use { output ->
                val buffer =
                    ByteArray(4 * 1024)
                var read: Int = -1
                while (input?.read(buffer).also {
                        if (it != null) {
                            read = it
                        }
                    } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }
        return file
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageSize = pageCount
    }


    private fun generateImageFromPdf(pdfUri: Uri) {
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
//            saveImage(bmp)
            Glide.with(requireActivity())
                .load(bmp)
                .into(binding().imageView3)
            pdfiumCore.closeDocument(pdfDocument) // important!
        } catch (e: Exception) {
            //todo with exception
        }
    }

    private fun saveImage(bmp: Bitmap) {
        var out: FileOutputStream? = null
        try {
            val folder = File(FOLDER)
            if (!folder.exists()) folder.mkdirs()
            val file = File(folder, "PDF.png")
            out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
        } catch (e: Exception) {
            //todo with exception
        } finally {
            try {
                out?.close()
            } catch (e: Exception) {
                //todo with exception
            }
        }
    }


    override fun loadComplete(nbPages: Int) {
        val meta: PdfDocument.Meta = binding().pdfViewAdmin.documentMeta
        Log.e("TAG", "title = " + meta.title)
        Log.e("TAG", "author = " + meta.author)
        Log.e("TAG", "subject = " + meta.subject)
        Log.e("TAG", "keywords = " + meta.keywords)
        Log.e("TAG", "creator = " + meta.creator)
        Log.e("TAG", "producer = " + meta.producer)
        Log.e("TAG", "creationDate = " + meta.creationDate)
        Log.e("TAG", "modDate = " + meta.modDate)

        printBookmarksTree(binding().pdfViewAdmin.tableOfContents, "-")
    }

    private fun printBookmarksTree(tree: List<PdfDocument.Bookmark>, sep: String) {
        for (b in tree) {
            Log.e("TAG", java.lang.String.format("%s %s, p %d", sep, b.title, b.pageIdx))
            if (b.hasChildren()) {
                printBookmarksTree(b.children, "$sep-")
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                launchPicker()
            }
        }
    }

    override fun onError(t: Throwable?) {
    }
}
