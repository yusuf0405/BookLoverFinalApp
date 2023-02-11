package com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.shockwave.pdfium.PdfiumCore
import java.io.ByteArrayOutputStream
import javax.inject.Inject

interface BookPdfHelper {

    fun fetchBookPosterFromPdfUri(context: Context, pdfUri: Uri): Pair<Bitmap?, ByteArray?>
}

class BookPdfHelperImpl @Inject constructor() : BookPdfHelper {

    override fun fetchBookPosterFromPdfUri(
        context: Context,
        pdfUri: Uri
    ): Pair<Bitmap?, ByteArray?> {
        val pageNumber = 0
        val pdfiumCore = PdfiumCore(context)
        try {
            val fd = context.contentResolver.openFileDescriptor(pdfUri, "r")!!
            val pdfDocument = pdfiumCore.newDocument(fd)
            pdfiumCore.openPage(pdfDocument, pageNumber)
            val width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber)
            val height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height)
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()

            pdfiumCore.closeDocument(pdfDocument)
            return Pair(bmp, byteArray)
        } catch (e: Exception) {
            return Pair(null, null)
        }
    }
}