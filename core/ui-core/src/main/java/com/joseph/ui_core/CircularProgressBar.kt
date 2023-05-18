package com.joseph.ui_core

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class CircularProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 40f
        strokeCap = Paint.Cap.ROUND
    }

    private val rectF = RectF()

    private var startAngle = 0f
    private var sweepAngle = 0f
    private var maxAngle = 360f

    private val animator = ValueAnimator()

    init {
        // Инициализируем аниматор
        animator.apply {
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Float
                sweepAngle = value
                invalidate()
            }
        }
    }

    fun setMaxAngle(angle: Float) {
        maxAngle = angle
    }

    fun setProgress(progress: Float, duration: Long = 1000) {
        animator.cancel()
        animator.setFloatValues(sweepAngle, progress * maxAngle / 100)
        animator.duration = duration
        animator.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = paint.strokeWidth / 2f
        rectF.set(padding, padding, w - padding, h - padding)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawArc(rectF, startAngle, sweepAngle, false, paint)
    }
}
