package com.joseph.ui.core.custom.progress_bar

class Segment {

    private var animationProgress: Int = 0

    var animationState: AnimationState = AnimationState.IDLE
        set(value) {
            animationProgress = when (value) {
                AnimationState.ANIMATED -> 100
                AnimationState.IDLE -> 0
                else -> animationProgress
            }
            field = value
        }

    /**
     * Represents possible drawing states of the segment
     */
    enum class AnimationState {
        ANIMATED,
        ANIMATING,
        IDLE
    }

    val progressPercentage: Float
        get() = animationProgress.toFloat() / 100

    fun progress() = animationProgress++
}