//package com.joseph.ui_core.custom.pdf
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.pdf.PdfDocument
//import android.graphics.pdf.PdfRenderer
//import android.util.AttributeSet
//import android.view.GestureDetector
//import android.view.MotionEvent
//import android.view.View
//import androidx.core.view.GestureDetectorCompat
//
//class PdfViewer @JvmOverloads constructor(
//    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
//) : View(context, attrs, defStyleAttr) {
//
//    private var pdfDocument: PdfDocument? = null
//    private var currentPage: PdfRenderer.Page? = null
//    private var pdfRenderer: PdfRenderer? = null
//    private var pdfPageWidth: Int = 0
//    private var pdfPageHeight: Int = 0
//    private var displayWidth: Int = 0
//    private var displayHeight: Int = 0
//    private var xOffset: Float = 0f
//    private var yOffset: Float = 0f
//    private var bitmap: Bitmap? = null
//    private var backgroundColor: Int = Color.WHITE
//    private var textColor: Int = Color.BLACK
//    private var textSize: Float = 16f
//
//    private val gestureDetector: GestureDetectorCompat by lazy {
//        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
//            override fun onScroll(
//                e1: MotionEvent,
//                e2: MotionEvent,
//                distanceX: Float,
//                distanceY: Float
//            ): Boolean {
//                if (pdfDocument == null) {
//                    return false
//                }
//
//                xOffset += distanceX
//                yOffset += distanceY
//
//                val displayRect = getDisplayRect()
//                val bitmap = Bitmap.createBitmap(
//                    displayWidth, displayHeight, Bitmap.Config.ARGB_8888
//                )
//                val canvas = Canvas(bitmap)
//                canvas.drawColor(backgroundColor)
//
//                currentPage?.render(
//                    bitmap, null, displayRect, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
//                )
//
//                this@PdfViewer.bitmap = bitmap
//                invalidate()
//
//                return true
//            }
//
//            override fun onFling(
//                e1: MotionEvent,
//                e2: MotionEvent,
//                velocityX: Float,
//                velocityY: Float
//            ): Boolean {
//                if (pdfDocument == null) {
//                    return false
//                }
//
//                val x = velocityX / 10
//                val y = velocityY / 10
//                val displayRect = getDisplayRect()
//
//                val newXOffset = xOffset + x
//                val newYOffset = yOffset + y
//
//                xOffset = when {
//                    newXOffset < 0f -> 0f
//                    newXOffset + displayRect.width() > pdfPageWidth -> {
//                        pdfPageWidth.toFloat() - displayRect.width()
//                    }
//                    else -> newXOffset
//                }
//
//                yOffset = when {
//                    newYOffset < 0f -> 0f
//                    newYOffset + displayRect.height() > pdfPageHeight -> {
//                        pdfPageHeight.toFloat() - displayRect.height()
//                    }
//                    else -> newYOffset
//                }
//
//                val bitmap = Bitmap.createBitmap(
//                    displayWidth, displayHeight, Bitmap.Config.ARGB_8888
//                )
//                val canvas = Canvas(bitmap)
//                canvas.drawColor(backgroundColor)
//
//                currentPage?.render(
//                    bitmap, null, getDisplayRect(), PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
//                )
//
//                this@PdfViewer.bitmap = bitmap
//                invalidate()
//
//                return true
//            }
//        })
//    }
//    fun zoomIn() {
//        if (mScaleFactor < MAX_SCALE_FACTOR) {
//            mScaleFactor += SCALE_STEP
//            invalidate()
//        }
//    }
//
//    fun zoomOut() {
//        if (mScaleFactor > MIN_SCALE_FACTOR) {
//            mScaleFactor -= SCALE_STEP
//            invalidate()
//        }
//    }
//
//    fun scroll(dx: Float, dy: Float) {
//        mTranslationX += dx
//        mTranslationY += dy
//        invalidate()
//    }
//
//
//    init {
//        context.theme.obtainStyledAttributes(
//            attrs,
//            R.styleable.PdfViewer,
//            0,
//            0
//        ).apply {
//            try {
//                backgroundColor = getColor(
//                    R.styleable.PdfViewer_backgroundColor, Color.WHITE
//                )
//                textColor = getColor(
//                    R.styleable.PdfViewer_textColor, Color.BLACK
//                )
//                textSize = getDimension(
//                    R.styleable.PdfViewer_textSize, 16f
//                )
//            } finally {
//                recycle()
//            }
//        }
