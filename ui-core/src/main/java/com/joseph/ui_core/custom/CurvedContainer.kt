package com.joseph.ui_core.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.joseph.ui_core.R
import com.joseph.ui_core.extensions.toPx

/**
 * Класс позволяет скруглять верхнюю часть BottomSheet используя Canvas and XML.
 * Мы нарисуем путь и закрасим замкнутую область пути.
 * */
class CurvedContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private companion object {
        private val CORNER_RADIUS = 32.toPx
    }

    private val mPaintRect = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setBackgroundColor(Color.TRANSPARENT)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        mPaintRect.apply {
            style = Paint.Style.FILL_AND_STROKE
            color = applyColor(attrs, defStyleAttr)
            isAntiAlias = true
            clipChildren = true
        }
    }

    private fun applyColor(attrs: AttributeSet?, defStyleAttr: Int) =
        obtainAttrs(attrs, defStyleAttr).let {
            val color = it.getColor(
                R.styleable.CurvedContainer_containerBackground,
                resources.getColor(R.color.white_smoke)
            )
            it.recycle()
            color
        }

    private fun obtainAttrs(attrs: AttributeSet?, defStyleAttr: Int) =
        context.obtainStyledAttributes(attrs, R.styleable.CurvedContainer, defStyleAttr, 0)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setBackgroundColor(Color.TRANSPARENT)
        canvas?.drawRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            CORNER_RADIUS.toFloat(),
            CORNER_RADIUS.toFloat(),
            mPaintRect
        )
        canvas?.drawRect(RectF(0f, height / 2f, width.toFloat(), height.toFloat()), mPaintRect)
    }
}