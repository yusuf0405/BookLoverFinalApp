package com.joseph.ui_core.custom.progress_bar

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.joseph.ui_core.R

class StoriesProgressBar(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
    ProgressBar(context, attributeSet, defStyle) {

    private var segments = mutableListOf<Segment>()
    val segmentWidth: Float
        get() = (measuredWidth - margin * (segmentCount - 1)).toFloat() / segmentCount

    var segmentStrokeWidth: Int =
        resources.getDimensionPixelSize(R.dimen.default_segment_stroke_width)
        private set

    var margin: Int = resources.getDimensionPixelSize(R.dimen.default_segment_margin)
        private set

    var segmentCount: Int = resources.getInteger(R.integer.default_segments_count)
        set(value) {
            field = value
            this.initSegments()
        }

    var segmentBackgroundColor: Int = Color.WHITE
        private set
    var segmentSelectedBackgroundColor: Int =
        Color.BLACK

        private set
    var segmentStrokeColor: Int = Color.BLACK
        private set
    var segmentSelectedStrokeColor: Int = Color.BLACK
        private set

    var radius: Int = resources.getDimensionPixelSize(R.dimen.default_corner_radius)
        private set

    //Drawing
    val strokeApplicable: Boolean
        get() = segmentStrokeWidth * 4 <= measuredHeight


    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context) : this(context, null)

    private var objectAnimator = ObjectAnimator.ofInt(this, "progress", 0, 10000)
    private var listener: Listener? = null
    private var hasCanceled = false

    init {
        initView()
    }

    private fun initSegments() {
        this.segments.clear()
        segments.addAll(List(segmentCount) { Segment() })
        this.invalidate()
        reset()
    }

    fun reset() {
        this.segments.map { it.animationState = Segment.AnimationState.IDLE }
        this.invalidate()
    }

    private fun initView() {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        params.marginEnd = context.resources.getDimensionPixelSize(R.dimen.stories_progress_margin)
        max = 10000
        progress = 0
        layoutParams = params
//        progressDrawable = ContextCompat.getDrawable(context, R.drawable.stories_progress_bar)
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                if (hasCanceled.not()) {
                    listener?.onProgressEnd()
                }
            }

            override fun onAnimationCancel(animation: Animator) {
                hasCanceled = true
            }

            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.i("Joseph","segments = ${segments.size}")
//        segments.forEachIndexed { index, segment ->
//            val drawingComponents = getDrawingComponents(segment, index)
//            drawingComponents.first.forEachIndexed { drawingIndex, rectangle ->
//                canvas?.drawRoundRect(
//                    rectangle,
//                    radius.toFloat(),
//                    radius.toFloat(),
//                    drawingComponents.second[drawingIndex]
//                )
//            }
//        }
        invalidate()
        requestLayout()
    }

    fun addListener(listener: Listener) {
        this.listener = listener
    }

    fun startProgress(durationInSeconds: Float) {
        val safetyDuration = if (durationInSeconds < 0) 0f else durationInSeconds
        hasCanceled = false
        cancelProgress()
        objectAnimator.apply {
            duration = (safetyDuration * 1000).toLong()
            start()
        }
    }

    fun cancelProgress() {
        objectAnimator.cancel()
    }

    fun pauseProgress() {
        objectAnimator.pause()
    }

    fun resumeProgress() {
        objectAnimator.resume()
    }

    fun interface Listener {
        fun onProgressEnd()
    }
}

fun SegmentedProgressBar.getDrawingComponents(
    segment: Segment,
    segmentIndex: Int
): Pair<MutableList<RectF>, MutableList<Paint>> {

    val rectangles = mutableListOf<RectF>()
    val paints = mutableListOf<Paint>()
    val segmentWidth = segmentWidth
    val startBound = segmentIndex * segmentWidth + ((segmentIndex) * margin)
    val endBound = startBound + segmentWidth
    val stroke = if (!strokeApplicable) 0f else this.segmentStrokeWidth.toFloat()

    val backgroundPaint = Paint().apply {
        style = android.graphics.Paint.Style.FILL
        color = segmentBackgroundColor
    }

    val selectedBackgroundPaint = Paint().apply {
        style = android.graphics.Paint.Style.FILL
        color = segmentSelectedBackgroundColor
    }

    val strokePaint = Paint().apply {
        color =
            if (segment.animationState == Segment.AnimationState.IDLE) segmentStrokeColor else segmentSelectedStrokeColor
        style = android.graphics.Paint.Style.STROKE
        strokeWidth = stroke
    }

    //Background component
    if (segment.animationState == Segment.AnimationState.ANIMATED) {
        rectangles.add(RectF(startBound + stroke, height - stroke, endBound - stroke, stroke))
        paints.add(selectedBackgroundPaint)
    } else {
        rectangles.add(RectF(startBound + stroke, height - stroke, endBound - stroke, stroke))
        paints.add(backgroundPaint)
    }

    //Progress component
    if (segment.animationState == Segment.AnimationState.ANIMATING) {
        rectangles.add(
            RectF(
                startBound + stroke,
                height - stroke,
                startBound + segment.progressPercentage * segmentWidth,
                stroke
            )
        )
        paints.add(selectedBackgroundPaint)
    }

    //Stroke component
    if (stroke > 0) {
        rectangles.add(RectF(startBound + stroke, height - stroke, endBound - stroke, stroke))
        paints.add(strokePaint)
    }

    return Pair(rectangles, paints)
}