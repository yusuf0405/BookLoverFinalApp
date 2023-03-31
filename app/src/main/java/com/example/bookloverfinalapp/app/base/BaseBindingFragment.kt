package com.example.bookloverfinalapp.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.bookloverfinalapp.app.utils.extensions.setPaddingTopHeightStatusBar
import com.joseph.ui_core.custom.snackbar.GenericSnackbar

abstract class BaseBindingFragment<V : ViewBinding>(
    private val binder: (LayoutInflater, ViewGroup?, Boolean) -> V,
) : Fragment() {

    private var viewBinding: V? = null

    var isFullScreen: Boolean = false
    protected fun binding(): V = checkNotNull(viewBinding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = binder.invoke(inflater, container, false)
        viewBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isFullScreen) binding().root.setPaddingTopHeightStatusBar()
    }
    fun showErrorSnackbar(message: String) =
        GenericSnackbar
            .Builder(binding().root)
            .error()
            .message(message)
            .build()
            .show()

    fun showSuccessSnackbar(message: String) =
        GenericSnackbar
            .Builder(binding().root)
            .success()
            .message(message)
            .build()
            .show()

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}