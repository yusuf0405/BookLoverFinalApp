package com.joseph.ui_core.custom.snackbar

import android.graphics.drawable.Drawable
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.joseph.ui_core.databinding.LayoutGenericSnackbarBinding
import com.joseph.ui_core.databinding.LayoutGenericSnackbarTimerBinding
import com.joseph.ui_core.extensions.layoutInflater

/**
 * Класс создает кастомный новый [Snackbar] под названием [GenericSnackbar].
 * Конструктор этого класса является закрытым, так что только код в этом файле может создать его экземпляр.
 * Это означает, что никакие другие файлы не могут получить доступ или изменить состояние этого объекта,
 * что делает его безопасным для использования в качестве синглтона.
 */
open class GenericSnackbar private constructor() {

    open class Builder(val rootView: View) {

        protected open var message: String? = emptyString()
        protected open var buttonText: String? = emptyString()
        protected open var buttonClickListener: View.OnClickListener? = null
        protected open var icon: Drawable? = null
        protected open var duration: Int = BaseTransientBottomBar.LENGTH_LONG

        private fun emptyString() = String()

        private fun iconsProvider() = GenericSnackbarIconsProvider.Base(rootView.context)

        fun timer() = TimerSnackbarBuilder(rootView = rootView)

        fun info() = InfoSnackbarBuilder(rootView = rootView, iconsProvider = iconsProvider())

        fun success() = SuccessSnackbarBuilder(rootView = rootView, iconsProvider = iconsProvider())

        fun warning() = WarningSnackbarBuilder(rootView = rootView, iconsProvider = iconsProvider())

        fun error() = ErrorSnackbarBuilder(rootView = rootView, iconsProvider = iconsProvider())

        open fun message(message: String): Builder {
            this.message = message
            return this
        }

        open fun buttonText(buttonText: String): Builder {
            this.buttonText = buttonText
            return this
        }

        open fun buttonClickListener(buttonClickListener: View.OnClickListener?): Builder {
            this.buttonClickListener = buttonClickListener
            return this
        }

        open fun duration(duration: Long): Builder {
            if (duration > MAX_SNACKBAR_DURATION) this.duration = MAX_SNACKBAR_DURATION
            else this.duration = duration.toInt()
            return this
        }

        open fun build() =
            assembleSnackbar(
                message = message,
                buttonText = buttonText,
                icon = icon,
                listener = buttonClickListener,
                duration = duration,
                rootView = rootView
            )
    }

    companion object {

        private const val MAX_SNACKBAR_DURATION = 99

        private fun assembleSnackbar(
            message: String?,
            buttonText: String?,
            icon: Drawable?,
            listener: View.OnClickListener?,
            duration: Int,
            rootView: View
        ): Snackbar {
            val binding =
                LayoutGenericSnackbarBinding.inflate(rootView.context.layoutInflater())

            val snackbar = inflateSnackBar(
                customLayout = binding.root,
                rootView = rootView,
                duration = duration
            )
            setSnackbarText(
                text = message,
                textView = binding.message
            )
            setSnackbarButtonText(
                textView = binding.button,
                divider = binding.divider,
                text = buttonText
            )
            setSnackbarImage(
                imageView = binding.icon,
                container = binding.iconContainer,
                drawable = icon
            )
            setSnackbarButtonClickListener(
                textView = binding.button,
                listener = listener,
                snackbar = snackbar
            )
            return snackbar
        }

        private fun assembleSnackbar(
            message: String?,
            buttonText: String?,
            listener: View.OnClickListener?,
            timeOverListener: TimeOverListener?,
            duration: Long,
            rootView: View
        ): Snackbar {
            val binding =
                LayoutGenericSnackbarTimerBinding.inflate(rootView.context.layoutInflater())
            val snackbar = inflateSnackBar(
                customLayout = binding.root,
                rootView = rootView,
                duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            )
            setSnackbarText(
                text = message,
                textView = binding.message
            )
            setSnackbarButtonText(
                textView = binding.button,
                divider = binding.divider,
                text = buttonText
            )
            setSnackbarButtonClickListener(
                textView = binding.button,
                listener = listener,
                snackbar = snackbar
            )
            binding.progressBar.progress = Int.MAX_VALUE

            val genericSnackbarBaseCallback = GenericSnackbarBaseCallbackImpl(
                progressBar = binding.progressBar,
                duration = duration,
                timeListener = timeOverListener,
                setSnackbarTextListener = { timeSeconds ->
                    setSnackbarText(textView = binding.timer, text = timeSeconds)
                },
                animationEndListener = {
                    snackbar.dismiss()
                }
            )

            snackbar.addCallback(genericSnackbarBaseCallback)
            return snackbar
        }

        private fun inflateSnackBar(customLayout: View, rootView: View, duration: Int) =
            GenericSnackbarInflter.Base()
                .inflate(
                    rootView = rootView,
                    customLayout = customLayout,
                    duration = duration
                )

        private fun setSnackbarText(textView: TextView, text: String?) {
            textView.text = text
        }

        private fun setSnackbarButtonText(textView: TextView, divider: View, text: String?) {
            textView.text = text
            val buttonVisibility = !text.isNullOrEmpty()
            textView.isVisible = buttonVisibility
            divider.isVisible = buttonVisibility
        }

        private fun setSnackbarButtonClickListener(
            textView: TextView,
            listener: View.OnClickListener?,
            snackbar: Snackbar
        ) {
            textView.setOnClickListener { button ->
                snackbar.dismiss()
                listener?.onClick(button)
            }
        }

        private fun setSnackbarImage(
            imageView: ImageView,
            container: FrameLayout,
            drawable: Drawable?
        ) {
            imageView.setImageDrawable(drawable)
            container.isVisible = drawable != null
        }
    }

    class TimerSnackbarBuilder(rootView: View) : Builder(rootView) {
        override var duration: Int = 5
        private var timeOverListener: TimeOverListener? = null

        fun timeOverListener(timeOverListener: TimeOverListener): TimerSnackbarBuilder {
            this.timeOverListener = timeOverListener
            return this
        }

        override fun build() = assembleSnackbar(
            message = message,
            buttonText = buttonText,
            listener = buttonClickListener,
            timeOverListener = timeOverListener,
            duration = duration.toLong(),
            rootView = rootView

        )
    }

    class InfoSnackbarBuilder(
        rootView: View,
        iconsProvider: GenericSnackbarIconsProvider
    ) : Builder(rootView) {
        override var icon = iconsProvider.infoSnackbarIcon()
    }

    class SuccessSnackbarBuilder(
        rootView: View,
        iconsProvider: GenericSnackbarIconsProvider
    ) : Builder(rootView) {
        override var icon = iconsProvider.successSnackbarIcon()
    }

    class WarningSnackbarBuilder(
        rootView: View,
        iconsProvider: GenericSnackbarIconsProvider
    ) : Builder(rootView) {
        override var icon = iconsProvider.warningSnackbarIcon()
    }

    class ErrorSnackbarBuilder(
        rootView: View,
        iconsProvider: GenericSnackbarIconsProvider
    ) : Builder(rootView) {
        override var icon = iconsProvider.errorSnackbarIcon()
    }
}