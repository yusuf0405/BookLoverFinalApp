package com.joseph.ui.core.custom

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Layout
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.joseph.ui.core.R

class FadingTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val drawRect = RectF()
    private val realRect = Rect()
    private val selection = Path()
    private val paint = Paint()
    private var fadeLength = 0.0f
    private var exceptLastNLetters = 0
    private var shader =
        LinearGradient(0f, 0f, 1f, 0f, 0x00000000, -0x1000000, Shader.TileMode.CLAMP)

    init {

        if (attrs != null) {
            val arr =
                context.obtainStyledAttributes(attrs, R.styleable.FadingTextView, defStyleAttr, 0)
            exceptLastNLetters = arr.getInt(R.styleable.FadingTextView_except_last_the_letters, 0)
            fadeLength = arr.getFloat(R.styleable.FadingTextView_fade_length, 0.0f)

            val hideLastFadedLetters =
                arr.getBoolean(R.styleable.FadingTextView_hide_last_faded_letters, false)
            if (hideLastFadedLetters) {
                shader =
                    LinearGradient(0f, 0f, 0.5f, 0f, 0x00000000, -0x1000000, Shader.TileMode.CLAMP)
            }

            arr.recycle()
        }
        paint.shader = shader
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }

    fun setExceptLastNLetters(characterCount: Int) {
        exceptLastNLetters = characterCount
        invalidateView()
    }

    fun setFadeLength(fadeLength: Float) {
        this.fadeLength = fadeLength
        invalidateView()
    }

    private fun invalidateView() {
        requestLayout()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val drawBounds = drawRect
        val realBounds = realRect
        val selectionPath = selection
        val layout = layout
        // Индекс последней строки рисунка и смещения текста
        val lastLineIndex = lineCount - 1
        val lastLineStart = layout.getLineStart(lastLineIndex)
        val lastLineEnd = layout.getLineEnd(lastLineIndex)
        // Позволяем макету создать контур, который будет охватывать текст последней строки
        layout.getSelectionPath(lastLineStart, lastLineEnd - exceptLastNLetters, selectionPath)
        // Преобразуем этот Path в RectF, который нам легче модифицировать
        selectionPath.computeBounds(drawBounds, false)
        // Наивное определение направления текста; может потребоваться уточнение
        val isRtl = layout.getParagraphDirection(lastLineIndex) == Layout.DIR_RIGHT_TO_LEFT
        // Сужение границ только до длины затухания
        if (isRtl) drawBounds.right = drawBounds.left + drawBounds.width() * fadeLength
        else drawBounds.left = drawBounds.right - drawBounds.width() * fadeLength
        // Корректировка выдвижных элементов и отступов
        drawBounds.offset(totalPaddingLeft.toFloat(), totalPaddingTop.toFloat())
        // Преобразование границ рисунка в реальные границы для определения
        // если нам нужно сделать затухание, или обычный рисунок
        drawBounds.round(realBounds)
        realBounds.offset(-scrollX, -scrollY)
        val needToFade: Boolean = realBounds.intersects(
            totalPaddingLeft,
            totalPaddingTop,
            width - totalPaddingRight,
            height - totalPaddingBottom
        )
        if (needToFade) {
            // Настройка и установка матрицы шейдеров
            val shaderMatrix = matrix
            shaderMatrix.reset()
            shaderMatrix.setScale(drawBounds.width(), 1f)
            if (isRtl) shaderMatrix.postRotate(380f, drawBounds.width() / 2f, 0f)
            shaderMatrix.postTranslate(drawBounds.left, drawBounds.top)
            shader.setLocalMatrix(shaderMatrix)
            // Сохраняем и начать рисовать во внеэкранный буфер
            val saveCount = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                canvas.saveLayer(null, null)
            else canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
            // Позволяем TextView нарисовать себя в буфере
            super.onDraw(canvas)
            // Рисуем затухание в буфере, поверх содержимого TextView
            canvas.drawRect(drawBounds, paint)
            // Восстановливаем и перерисовать буфер обратно на холст
            canvas.restoreToCount(saveCount)
        } else super.onDraw(canvas)
    }
}