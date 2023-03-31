package com.joseph.ui_core.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import com.joseph.ui_core.R


class FadingEdgeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var fadeTop = false
    private var fadeBottom = false
    private var fadeLeft = false
    private var fadeRight = false
    private var gradientSizeTop = 0
    private var gradientSizeBottom = 0
    private var gradientSizeLeft = 0
    private var gradientSizeRight = 0
    private var gradientPaintTop = Paint(Paint.ANTI_ALIAS_FLAG)
    private var gradientPaintBottom = Paint(Paint.ANTI_ALIAS_FLAG)
    private var gradientPaintLeft = Paint(Paint.ANTI_ALIAS_FLAG)
    private var gradientPaintRight = Paint(Paint.ANTI_ALIAS_FLAG)
    private var gradientRectTop = Rect()
    private var gradientRectBottom = Rect()
    private var gradientRectLeft = Rect()
    private var gradientRectRight = Rect()
    private var gradientDirtyFlags = 0

    init {
        val defaultSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GRADIENT_SIZE_DP.toFloat(),
            resources.displayMetrics
        ).toInt()
        if (attrs != null) {
            val arr =
                context.obtainStyledAttributes(attrs, R.styleable.FadingEdgeLayout, defStyleAttr, 0)
            val flags = arr.getInt(R.styleable.FadingEdgeLayout_fade_edge, 0)
            fadeTop = flags and FADE_EDGE_TOP == FADE_EDGE_TOP
            fadeBottom = flags and FADE_EDGE_BOTTOM == FADE_EDGE_BOTTOM
            fadeLeft = flags and FADE_EDGE_LEFT == FADE_EDGE_LEFT
            fadeRight = flags and FADE_EDGE_RIGHT == FADE_EDGE_RIGHT
            gradientSizeTop =
                arr.getDimensionPixelSize(R.styleable.FadingEdgeLayout_fade_size_top, defaultSize)
            gradientSizeBottom =
                arr.getDimensionPixelSize(
                    R.styleable.FadingEdgeLayout_fade_size_bottom,
                    defaultSize
                )
            gradientSizeLeft =
                arr.getDimensionPixelSize(R.styleable.FadingEdgeLayout_fade_size_left, defaultSize)
            gradientSizeRight =
                arr.getDimensionPixelSize(R.styleable.FadingEdgeLayout_fade_size_right, defaultSize)
            if (fadeTop && gradientSizeTop > 0) {
                gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_TOP
            }
            if (fadeLeft && gradientSizeLeft > 0) {
                gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_LEFT
            }
            if (fadeBottom && gradientSizeBottom > 0) {
                gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_BOTTOM
            }
            if (fadeRight && gradientSizeRight > 0) {
                gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_RIGHT
            }
            arr.recycle()
        } else {
            gradientSizeRight = defaultSize
            gradientSizeLeft = gradientSizeRight
            gradientSizeBottom = gradientSizeLeft
            gradientSizeTop = gradientSizeBottom
        }
        val mode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        gradientPaintTop.xfermode = mode
        gradientPaintBottom.xfermode = mode
        gradientPaintLeft.xfermode = mode
        gradientPaintRight.xfermode = mode
    }

    fun setFadeSizes(top: Int, left: Int, bottom: Int, right: Int) {
        if (gradientSizeTop != top) {
            gradientSizeTop = top
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_TOP
        }
        if (gradientSizeLeft != left) {
            gradientSizeLeft = left
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_LEFT
        }
        if (gradientSizeBottom != bottom) {
            gradientSizeBottom = bottom
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_BOTTOM
        }
        if (gradientSizeRight != right) {
            gradientSizeRight = right
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_RIGHT
        }
        if (gradientDirtyFlags != 0) {
            invalidate()
        }
    }

    fun setFadeEdges(fadeTop: Boolean, fadeLeft: Boolean, fadeBottom: Boolean, fadeRight: Boolean) {
        if (this.fadeTop != fadeTop) {
            this.fadeTop = fadeTop
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_TOP
        }
        if (this.fadeLeft != fadeLeft) {
            this.fadeLeft = fadeLeft
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_LEFT
        }
        if (this.fadeBottom != fadeBottom) {
            this.fadeBottom = fadeBottom
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_BOTTOM
        }
        if (this.fadeRight != fadeRight) {
            this.fadeRight = fadeRight
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_RIGHT
        }
        if (gradientDirtyFlags != 0) {
            invalidate()
        }
    }

    fun setFadeEffectInRight(fadeSize: Int) {
        setFadeEdges(
            fadeTop = false,
            fadeLeft = false,
            fadeBottom = false,
            fadeRight = true
        )
        setFadeSizes(top = 0, bottom = 0, left = 0, right = fadeSize)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        if (paddingLeft != left) {
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_LEFT
        }
        if (paddingTop != top) {
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_TOP
        }
        if (paddingRight != right) {
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_RIGHT
        }
        if (paddingBottom != bottom) {
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_BOTTOM
        }
        super.setPadding(left, top, right, bottom)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw) {
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_LEFT
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_RIGHT
        }
        if (h != oldh) {
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_TOP
            gradientDirtyFlags = gradientDirtyFlags or DIRTY_FLAG_BOTTOM
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        val newWidth = width
        val newHeight = height
        val fadeAnyEdge = fadeTop || fadeBottom || fadeLeft || fadeRight
        if (visibility == GONE || newWidth == 0 || newHeight == 0 || !fadeAnyEdge) {
            super.dispatchDraw(canvas)
            return
        }
        if (gradientDirtyFlags and DIRTY_FLAG_TOP == DIRTY_FLAG_TOP) {
            gradientDirtyFlags = gradientDirtyFlags and DIRTY_FLAG_TOP.inv()
            initTopGradient()
        }
        if (gradientDirtyFlags and DIRTY_FLAG_LEFT == DIRTY_FLAG_LEFT) {
            gradientDirtyFlags = gradientDirtyFlags and DIRTY_FLAG_LEFT.inv()
            initLeftGradient()
        }
        if (gradientDirtyFlags and DIRTY_FLAG_BOTTOM == DIRTY_FLAG_BOTTOM) {
            gradientDirtyFlags = gradientDirtyFlags and DIRTY_FLAG_BOTTOM.inv()
            initBottomGradient()
        }
        if (gradientDirtyFlags and DIRTY_FLAG_RIGHT == DIRTY_FLAG_RIGHT) {
            gradientDirtyFlags = gradientDirtyFlags and DIRTY_FLAG_RIGHT.inv()
            initRightGradient()
        }
        val count = canvas.saveLayer(
            0.0f,
            0.0f,
            width.toFloat(),
            height.toFloat(),
            null,
            Canvas.ALL_SAVE_FLAG
        )
        super.dispatchDraw(canvas)
        if (fadeTop && gradientSizeTop > 0) {
            canvas.drawRect(gradientRectTop, gradientPaintTop)
        }
        if (fadeBottom && gradientSizeBottom > 0) {
            canvas.drawRect(gradientRectBottom, gradientPaintBottom)
        }
        if (fadeLeft && gradientSizeLeft > 0) {
            canvas.drawRect(gradientRectLeft, gradientPaintLeft)
        }
        if (fadeRight && gradientSizeRight > 0) {
            canvas.drawRect(gradientRectRight, gradientPaintRight)
        }
        canvas.restoreToCount(count)
    }

    private fun initTopGradient() {
        val actualHeight = height - paddingTop - paddingBottom
        val size = gradientSizeTop.coerceAtMost(actualHeight)
        val l = paddingLeft
        val t = paddingTop
        val r = width - paddingRight
        val b = t + size
        gradientRectTop[l, t, r] = b
        val gradient =
            LinearGradient(
                l.toFloat(),
                t.toFloat(),
                l.toFloat(),
                b.toFloat(),
                FADE_COLORS,
                null,
                Shader.TileMode.CLAMP
            )
        gradientPaintTop.shader = gradient
    }

    private fun initLeftGradient() {
        val actualWidth = width - paddingLeft - paddingRight
        val size = gradientSizeLeft.coerceAtMost(actualWidth)
        val l = paddingLeft
        val t = paddingTop
        val r = l + size
        val b = height - paddingBottom
        gradientRectLeft[l, t, r] = b
        val gradient =
            LinearGradient(
                l.toFloat(),
                t.toFloat(),
                r.toFloat(),
                t.toFloat(),
                FADE_COLORS,
                null,
                Shader.TileMode.CLAMP
            )
        gradientPaintLeft.shader = gradient
    }

    private fun initBottomGradient() {
        val actualHeight = height - paddingTop - paddingBottom
        val size = gradientSizeBottom.coerceAtMost(actualHeight)
        val l = paddingLeft
        val t = paddingTop + actualHeight - size
        val r = width - paddingRight
        val b = t + size
        gradientRectBottom[l, t, r] = b
        val gradient =
            LinearGradient(
                l.toFloat(),
                t.toFloat(),
                l.toFloat(),
                b.toFloat(),
                FADE_COLORS_REVERSE,
                null,
                Shader.TileMode.CLAMP
            )
        gradientPaintBottom.shader = gradient
    }

    private fun initRightGradient() {
        val actualWidth = width - paddingLeft - paddingRight
        val size = gradientSizeRight.coerceAtMost(actualWidth)
        val l = paddingLeft + actualWidth - size
        val t = paddingTop
        val r = l + size
        val b = height - paddingBottom
        gradientRectRight[l, t, r] = b
        val gradient =
            LinearGradient(
                l.toFloat(),
                t.toFloat(),
                r.toFloat(),
                t.toFloat(),
                FADE_COLORS_REVERSE,
                null,
                Shader.TileMode.CLAMP
            )
        gradientPaintRight.shader = gradient
    }

    companion object {
        private const val DEFAULT_GRADIENT_SIZE_DP = 80
        const val FADE_EDGE_TOP = 1
        const val FADE_EDGE_BOTTOM = 2
        const val FADE_EDGE_LEFT = 4
        const val FADE_EDGE_RIGHT = 8
        private const val DIRTY_FLAG_TOP = 1
        private const val DIRTY_FLAG_BOTTOM = 2
        private const val DIRTY_FLAG_LEFT = 4
        private const val DIRTY_FLAG_RIGHT = 8
        private val FADE_COLORS = intArrayOf(Color.TRANSPARENT, Color.BLACK)
        private val FADE_COLORS_REVERSE = intArrayOf(Color.BLACK, Color.TRANSPARENT)
    }
}