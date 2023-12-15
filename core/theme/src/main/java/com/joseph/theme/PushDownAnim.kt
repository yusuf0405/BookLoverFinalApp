package com.joseph.theme

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.graphics.Rect
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.animation.AccelerateDecelerateInterpolator
import java.lang.ref.WeakReference

class PushDownAnim private constructor(view: View) : PushDown {
    @Retention(AnnotationRetention.SOURCE)
    annotation class Mode

    private val defaultScale: Float
    private var mode = MODE_SCALE
    private var pushScale = DEFAULT_PUSH_SCALE
    private var pushStatic = DEFAULT_PUSH_STATIC
    private var durationPush = DEFAULT_PUSH_DURATION
    private var durationRelease = DEFAULT_RELEASE_DURATION
    private var interpolatorPush = DEFAULT_INTERPOLATOR
    private var interpolatorRelease = DEFAULT_INTERPOLATOR
    private val weakView: WeakReference<View> by lazy { WeakReference(view) }

    private var scaleAnimSet: AnimatorSet? = null

    private val viewHeight: Int
        get() = weakView.get()!!.measuredHeight
    private val viewWidth: Int
        get() = weakView.get()!!.measuredWidth

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            weakView.get()?.resources?.displayMetrics
        )
    }

    init {
        weakView.get()!!.isClickable = true
        defaultScale = view.scaleX
    }

    override fun setScale(scale: Float): PushDown {
        if (mode == MODE_SCALE) {
            pushScale = scale
        } else if (mode == MODE_STATIC_DP) {
            pushStatic = scale
        }
        return this
    }

    override fun setScale(@Mode mode: Int, scale: Float): PushDown {
        this.mode = mode
        this.setScale(scale)
        return this
    }

    override fun setDurationPush(duration: Long): PushDown {
        durationPush = duration
        return this
    }

    override fun setDurationRelease(duration: Long): PushDown {
        durationRelease = duration
        return this
    }

    override fun setInterpolatorPush(interpolatorPush: AccelerateDecelerateInterpolator): PushDown {
        this.interpolatorPush = interpolatorPush
        return this
    }

    override fun setInterpolatorRelease(interpolatorRelease: AccelerateDecelerateInterpolator): PushDown {
        this.interpolatorRelease = interpolatorRelease
        return this
    }

    override fun setOnClickListener(clickListener: View.OnClickListener): PushDown {
        if (weakView.get() != null) {
            weakView.get()!!.setOnClickListener(clickListener)
        }
        return this
    }

    override fun setOnLongClickListener(clickListener: OnLongClickListener): PushDown {
        if (weakView.get() != null) {
            weakView.get()!!.setOnLongClickListener(clickListener)
        }
        return this
    }

    override fun setOnTouchEvent(eventListener: OnTouchListener?): PushDown {
        if (weakView.get() == null) return this
        if (eventListener == null) {
            weakView.get()!!.setOnTouchListener(object : OnTouchListener {
                var isOutSide = false
                var rect: Rect? = null

                override fun onTouch(
                    view: View,
                    motionEvent: MotionEvent
                ): Boolean {
                    if (!view.isClickable) return false
                    val action = motionEvent.action
                    if (action == MotionEvent.ACTION_DOWN) {
                        isOutSide = false
                        rect = Rect(
                            view.left,
                            view.top,
                            view.right,
                            view.bottom
                        )
                        makeDecisionAnimScale(
                            view,
                            mode,
                            pushScale,
                            pushStatic,
                            durationPush,
                            interpolatorPush,
                        )
                    } else if (action == MotionEvent.ACTION_MOVE) {
                        if (rect != null && !isOutSide
                            && !rect!!.contains(
                                view.left + motionEvent.x.toInt(),
                                view.top + motionEvent.y.toInt()
                            )
                        ) {
                            isOutSide = true
                            makeDecisionAnimScale(
                                view,
                                mode,
                                defaultScale,
                                0f,
                                durationRelease,
                                interpolatorRelease,
                            )
                        }
                    } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                        makeDecisionAnimScale(
                            view,
                            mode,
                            defaultScale,
                            0f,
                            durationRelease,
                            interpolatorRelease,
                        )
                    }
                    return false
                }
            })
        } else {
            weakView.get()?.setOnTouchListener { v, motionEvent ->
                eventListener.onTouch(
                    weakView.get(),
                    motionEvent
                )
            }
        }
        return this
    }

    private fun makeDecisionAnimScale(
        view: View,
        @Mode mode: Int,
        pushScale: Float,
        pushStatic: Float,
        duration: Long,
        interpolator: TimeInterpolator,
    ) {
        var tmpScale = pushScale
        if (mode == MODE_STATIC_DP) {
            tmpScale = getScaleFromStaticSize(pushStatic)
        }
        animScale(view, tmpScale, duration, interpolator)
    }

    private fun animScale(
        view: View,
        scale: Float,
        duration: Long,
        interpolator: TimeInterpolator
    ) {
        view.animate().cancel()
        if (scaleAnimSet != null) {
            scaleAnimSet!!.cancel()
        }
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale)
        scaleX.interpolator = interpolator
        scaleX.duration = duration
        scaleY.interpolator = interpolator
        scaleY.duration = duration
        scaleAnimSet = AnimatorSet()
        scaleAnimSet!!
            .play(scaleX)
            .with(scaleY)
        scaleX.addUpdateListener {
            val p = view.parent as View
            p.invalidate()
        }
        scaleAnimSet!!.start()
    }

    private fun getScaleFromStaticSize(sizeStaticDp: Float): Float {
        if (sizeStaticDp <= 0) return defaultScale
        val sizePx = dpToPx(sizeStaticDp)
        return if (viewWidth > viewHeight) {
            if (sizePx > viewWidth) return 1.0f
            val pushWidth = viewWidth - sizePx * 2
            pushWidth / viewWidth
        } else {
            if (sizePx > viewHeight) return 1.0f
            val pushHeight = viewHeight - sizePx * 2
            pushHeight / viewHeight.toFloat()
        }
    }

    companion object {
        const val DEFAULT_PUSH_SCALE = 0.97f
        const val DEFAULT_PUSH_STATIC = 2f
        const val DEFAULT_PUSH_DURATION: Long = 50
        const val DEFAULT_RELEASE_DURATION: Long = 125
        const val MODE_SCALE = 0
        const val MODE_STATIC_DP = 1
        val DEFAULT_INTERPOLATOR = AccelerateDecelerateInterpolator()
        fun setPushDownAnimTo(view: View): PushDownAnim {
            val pushAnim = PushDownAnim(view)
            pushAnim.setOnTouchEvent(null)
            return pushAnim
        }
    }
}
