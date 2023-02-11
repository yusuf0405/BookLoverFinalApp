package com.joseph.ui_core.custom.modal_page

import android.app.Activity
import android.app.Dialog
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.joseph.ui_core.R
import com.joseph.ui_core.databinding.FragmentModalPageBinding


// Актуализировать коэффициент после внедрения drag-логики
private const val MODAL_PAGE_SHRINK_RATIO = -0.2f

open class ModalPageFragment internal constructor(
    private val titleText: String = "",
    private val showCloseIcon: Boolean = false,
    private val cancelable: Boolean = true,
    private val minHeight: Float = 0f,
    private val maxHeight: Float = 1f,
    private val fragment: Fragment? = null
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentModalPageBinding
    private lateinit var viewModel: ModalPageViewModel
    lateinit var title: TextView
    lateinit var header: TextView
    lateinit var closeIcon: ImageView
    lateinit var toolbar: FrameLayout
    lateinit var handle: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ModalPage_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModalPageBinding.inflate(inflater)
        title = binding.title
        header = binding.header
        closeIcon = binding.closeIcon
        toolbar = binding.toolbar
        handle = binding.handle
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        // Замена onBackPressed()
        dialog.setOnKeyListener { _, keyCode, keyEvent ->
            // Проверка по двум параметрам для избежания повторного вызова
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                if (childFragmentManager.backStackEntryCount > 1) {
                    childFragmentManager.popBackStackImmediate()
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }

        var lastSlideOffset = 0.0f
        dialog.setOnShowListener {
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(bottomSheet).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_SETTLING) {
                            state = if (lastSlideOffset <= MODAL_PAGE_SHRINK_RATIO) {
                                dismissAllowingStateLoss() //  На случай оставшегося затемнения после сокрытия
                                BottomSheetBehavior.STATE_HIDDEN
                            } else {
                                BottomSheetBehavior.STATE_EXPANDED
                            }
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        lastSlideOffset = slideOffset
                        //  На случай, если при сокрытии карточки state не переходит из Collapsed в Hidden автоматически
                        if (lastSlideOffset == MODAL_PAGE_SHRINK_RATIO) {
                            state = BottomSheetBehavior.STATE_HIDDEN
                        }
                    }
                })
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setWindowAnimations(R.style.ModalPage_Animation)
        isCancelable = cancelable
        binding.handle.isVisible = !isCancelable
        val dialog = this.dialog as BottomSheetDialog
        dialog.apply {
            val behavior = BottomSheetBehavior.from(binding.rootContainer)
            if (!isCancelable) {
                setupRatio(dialog)
                behavior.peekHeight = (requireActivity().getScreenHeight() * minHeight).toInt()
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                behavior.peekHeight = requireActivity().getScreenHeight()
            }
            view.requestLayout()
        }
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ModalPageViewModel::class.java]
        with(binding) {
            title.text = viewModel.title ?: titleText
            header.text = viewModel.header ?: titleText

            header.isVisible = viewModel.headerVisible ?: titleText.isNotEmpty()
            title.isVisible = viewModel.titleVisible ?: !header.isVisible
            closeIcon.isVisible = viewModel.closeIconVisible ?: showCloseIcon
            toolbar.isVisible = isToolbarVisible()

            (toolbar.layoutParams as LinearLayout.LayoutParams).bottomMargin =
                if (closeIcon.isVisible && (title.text.isEmpty() && header.text.isEmpty())) 0
                else 16


            if (fragment != null) {
                childFragmentManager.beginTransaction()
                    .add(R.id.container, fragment)
                    .addToBackStack(ModalPage.TAG)
                    .commit()
            }

            closeIcon.setOnClickListener {
                dismissAllowingStateLoss()
            }

            backIcon.setOnClickListener {
                if (childFragmentManager.backStackEntryCount > 1) {
                    childFragmentManager.popBackStackImmediate()
                } else {
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
                setTitle("")
                backIcon.isVisible = false
            }

            content.setBackgroundResource(R.drawable.background_modal_page)

            childFragmentManager.registerFragmentLifecycleCallbacks(object :
                FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentResumed(fragmentManager: FragmentManager, f: Fragment) {
                    super.onFragmentResumed(fragmentManager, f)
                    backIcon.isVisible = fragmentManager.backStackEntryCount > 1
                    title.isVisible = fragmentManager.backStackEntryCount > 1
                    header.isVisible = fragmentManager.backStackEntryCount <= 1
                }

            }, true)
        }
    }

    private fun isToolbarVisible() =
        ((title.isVisible || header.isVisible) && (title.text.isNotEmpty() || header.text.isNotEmpty()) || closeIcon.isVisible)

    // Удаляем дубликат при пересоздании экрана
    override fun show(manager: FragmentManager, tag: String?) {
        val fragment = manager.findFragmentByTag(tag)
        if (manager.findFragmentByTag(tag) != null && fragment != null) {
            val fragmentTransaction = manager.beginTransaction()
            fragmentTransaction.remove(fragment).commit()
        }
        super.show(manager, tag)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.apply {
            title = binding.title.text.toString()
            titleVisible = binding.title.isVisible
            header = binding.header.text.toString()
            headerVisible = binding.header.isVisible
            closeIconVisible = binding.closeIcon.isVisible
        }
        super.onSaveInstanceState(outState)
    }

    fun setTitle(titleText: String) {
        with(binding) {
            title.text = titleText
            if (titleText.isNotEmpty()) {
                header.isVisible = false
                title.isVisible = true
                toolbar.isVisible = isToolbarVisible()
            } else {
                header.isVisible = true
                title.isVisible = false
                toolbar.isVisible = isToolbarVisible()
            }
        }
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return (requireActivity().getScreenHeight() * maxHeight).toInt()
    }

    private fun Activity.getScreenHeight(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }
}

fun AppCompatActivity.findModalPage(): ModalPageFragment? {
    return supportFragmentManager.findFragmentByTag(ModalPage.TAG) as ModalPageFragment?
}

fun AppCompatActivity.dismissModalPage() {
    findModalPage().let {
        (it as BottomSheetDialogFragment).dismissAllowingStateLoss()
    }
}

fun Fragment.findModalPage(): ModalPageFragment? {
    return parentFragmentManager.findFragmentByTag(ModalPage.TAG) as ModalPageFragment?
        ?: activity?.supportFragmentManager?.findFragmentByTag(ModalPage.TAG) as ModalPageFragment?
        ?: childFragmentManager.findFragmentByTag(ModalPage.TAG) as ModalPageFragment?
        ?: this as? ModalPageFragment?
}

fun Fragment.dismissModalPage() {
    findModalPage()?.let {
        (it as BottomSheetDialogFragment).dismiss()
    }
}