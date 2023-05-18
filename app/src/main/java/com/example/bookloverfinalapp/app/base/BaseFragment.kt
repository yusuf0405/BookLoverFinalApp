package com.example.bookloverfinalapp.app.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.utils.dialog.LoadingDialog
import com.example.bookloverfinalapp.app.utils.extensions.hide
import com.example.bookloverfinalapp.app.utils.extensions.navigateTo
import com.example.bookloverfinalapp.app.utils.extensions.setPaddingTopHeightStatusBar
import com.example.bookloverfinalapp.app.utils.extensions.show
import com.example.bookloverfinalapp.app.utils.navigation.NavigationCommand
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.joseph.ui_core.custom.snackbar.GenericSnackbar
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.joseph.utils_core.extensions.getAttrColor

abstract class BaseFragment<V : ViewBinding, VM : BaseViewModel>(
    private val binder: (LayoutInflater, ViewGroup?, Boolean) -> V,
) : Fragment() {

    protected abstract val viewModel: VM

    private var isCustomLoadingDialog: Boolean = false

    protected var isFullScreen: Boolean = false

    protected var isHaveToolbar: Boolean = false

    private var viewBinding: V? = null

    protected fun binding(): V = checkNotNull(viewBinding)

    protected val currentUser: User by lazy(LazyThreadSafetyMode.NONE) {
        SharedPreferences().getCurrentUser(activity = requireActivity()) ?: User.unknown()
    }

    private val bottomNavigationView: BottomNavigationView? by lazy(LazyThreadSafetyMode.NONE) {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView) ?: null
    }

    protected val loadingDialog: LoadingDialog by lazy(LazyThreadSafetyMode.NONE) {
        LoadingDialog(context = requireContext(), getString(R.string.loading_please_wait))
    }

    override fun onStart() {
        super.onStart()
        if (isHaveToolbar) {
            requireActivity().apply {
                window.statusBarColor = getAttrColor(R.attr.toolbar_background)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (isHaveToolbar) {
            requireActivity().window.statusBarColor = Color.TRANSPARENT
        }
    }

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
        observeRecourse()
        if (!isFullScreen) binding().root.setPaddingTopHeightStatusBar()
    }

    private fun observeRecourse() = with(viewModel) {
        launchWhenViewStarted {
            navCommand.observe(findNavController()::navigateTo)
            isErrorMessageIdFlow.observe { showErrorSnackbar(it.format(requireContext())) }
            isErrorMessageFlow.observe(::showErrorSnackbar)
            showSuccessSnackbarFlow.observe { showSuccessSnackBar(it.format(requireContext())) }
        }
        collectNavigation(viewLifecycleOwner) {
            it.getValue()?.let { navigationCommand ->
                handleNavigation(navigationCommand)
            }
        }
        collectProgressDialog(viewLifecycleOwner) { status ->
            if (isCustomLoadingDialog) return@collectProgressDialog
        }


        collectError(viewLifecycleOwner) {
            it.getValue()?.let { message ->
                showToast(message = message)
            }
        }
    }

    fun showBottomNavigationView() = bottomNavigationView?.apply { show() }

    fun hideBottomNavigationView() = bottomNavigationView?.apply { hide() }

    fun showToast(@StringRes messageRes: Int) {
        Toast.makeText(requireContext(), messageRes, Toast.LENGTH_SHORT).show()
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun handleNavigation(navCommand: NavigationCommand) {
        when (navCommand) {
            is NavigationCommand.ToDirection -> findNavController().navigate(navCommand.directions.actionId)
            is NavigationCommand.Back -> findNavController().navigateUp()
        }
    }

    fun showErrorSnackbar(message: String) =
        GenericSnackbar
            .Builder(binding().root)
            .error()
            .message(message)
            .build()
            .show()

    fun showSuccessSnackBar(message: String) =
        GenericSnackbar
            .Builder(binding().root)
            .success()
            .message(message)
            .build()
            .show()

    fun showInfoSnackBar(message: String) =
        GenericSnackbar
            .Builder(binding().root)
            .info()
            .message(message)
            .build()
            .show()

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    companion object {
        const val COLLAPSED = 1f
        const val EXPANDED = 0f
        const val DEFAULT_ITEMS_ANIMATOR_DURATION = 500L
    }

}