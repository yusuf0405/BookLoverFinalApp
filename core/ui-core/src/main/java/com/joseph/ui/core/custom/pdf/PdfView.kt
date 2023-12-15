//package com.joseph.ui.core.custom.pdf
//
//class PdfView @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : View(context, attrs, defStyleAttr) {
//
//    private var pdfPage: PdfRenderer.Page? = null
//    private var pdfRenderer: PdfRenderer? = null
//    private var pageNumber = 0
//    private var pdfFileUri: Uri? = null
//
//    private var scaledBitmap: Bitmap? = null
//    private var bitmapWidth = 0
//    private var bitmapHeight = 0
//
//    private val paint = Paint().apply {
//        isAntiAlias = true
//        isFilterBitmap = true
//        isDither = true
//    }
//
//    private var backgroundColor = Color.WHITE
//    private var fontSize = 12f
//
//    init {
//        context.theme.obtainStyledAttributes(
//            attrs,
//            R.styleable.PdfView,
//            defStyleAttr,
//            0
//        ).apply {
//            try {
//                backgroundColor = getColor(
//                    R.styleable.PdfView_backgroundColor,
//                    Color.WHITE
//                )
//                fontSize = getDimension(
//                    R.styleable.PdfView_fontSize,
//                    12f
//                )
//            } finally {
//                recycle()
//            }
//        }
//    }
//
//    fun setPdfFile(fileUri: Uri) {
//        pdfFileUri = fileUri
//        openPdfRenderer()
//    }
//
//    private fun openPdfRenderer() {
//        try {
//            pdfRenderer?.close()
//            pdfFileUri?.let { uri ->
//                pdfRenderer = PdfRenderer(context.contentResolver.openFileDescriptor(uri, "r")!!)
//                if (pdfRenderer!!.pageCount > 0) {
//                    pdfPage = pdfRenderer!!.openPage(pageNumber)
//                    bitmapWidth = pdfPage!!.width
//                    bitmapHeight = pdfPage!!.height
//                }
//            }
//            invalidate()
//        } catch (e: Exception) {
//            Log.e(TAG, "Error opening PDF Renderer", e)
//        }
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        canvas.drawColor(backgroundColor)
//
//        if (scaledBitmap != null) {
//            canvas.drawBitmap(scaledBitmap!!, 0f, 0f, paint)
//        }
//
//        pdfPage?.render(renderBitmap(), null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
//    }
//
//    private fun renderBitmap(): Bitmap {
//        val bmp = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
//        pdfPage?.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
//        return bmp
//    }
//
//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//        if (bitmapWidth > 0 && bitmapHeight > 0) {
//            val scaleX = w.toFloat() / bitmapWidth.toFloat()
//            val scaleY = h.toFloat() / bitmapHeight.toFloat()
//            val scale = scaleX.coerceAtMost(scaleY)
//
//            scaledBitmap = Bitmap.createScaledBitmap(
//                renderBitmap(),
//                (bitmapWidth * scale).toInt(),
//                (bitmapHeight * scale).toInt(),
//                true
//            )
//        }
//    }
//
//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        pdfPage?.close()
//        pdfRenderer?.close()
//    }
//
//    companion object {
//        private const val TAG = "PdfView"
//    }
//}
