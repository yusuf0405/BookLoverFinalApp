package com.joseph.core.motion

import androidx.constraintlayout.motion.widget.MotionLayout

class MotionListener @JvmOverloads constructor(
    private val callback: (motionState: MotionState) -> Unit = {},
) : MotionLayout.TransitionListener {
    override fun onTransitionStarted(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int
    ) {
        callback.invoke(MotionState.CHANGING)
    }

    override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
    }

    override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
        if ((motionLayout?.progress ?: return) == 1f) {
            callback.invoke(MotionState.COLLAPSED)
        }
        if (motionLayout.progress == 0f)
            callback.invoke(MotionState.EXPANDED)
    }

    override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
    ) {
        callback.invoke(MotionState.TRIGGERED)
    }
}