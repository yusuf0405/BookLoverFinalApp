package com.joseph.ui.core.custom.snackbar

import android.animation.ObjectAnimator
import android.os.CountDownTimer
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.google.android.material.snackbar.Snackbar

class GenericSnackbarBaseCallbackImpl(
    duration: Long,
    private val progressBar: ProgressBar,
    private var timeListener: TimeOverListener?,
    private val setSnackbarTextListener: (String) -> Unit,
    private val animationEndListener: () -> Unit,
) : GenericSnackbarBaseCallback() {

    private companion object {
        const val MILLS_IN_FUTURE = 1000
        const val COUNT_DOWN_INTERVAL = 1000L
        const val PROPERTY_NAME = "progress"
    }

    private var progressTimer = duration

    override fun onShown(transientBottomBar: Snackbar?) {
        ObjectAnimator.ofInt(progressBar, PROPERTY_NAME, 0).apply {
            this.duration = progressTimer * MILLS_IN_FUTURE
            interpolator = LinearInterpolator()
            doOnStart { startCountDownTimer() }
            doOnEnd { animationEndListener.invoke() }
        }.start()
    }

    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        timeListener = null
    }

    private fun startCountDownTimer() {
        object : CountDownTimer(progressTimer * MILLS_IN_FUTURE, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                if (progressTimer > 0) setSnackbarTextListener(progressTimer.toString())
                progressTimer--
            }

            override fun onFinish() {
                timeListener?.onTimeOver()
            }
        }.start()
    }
}