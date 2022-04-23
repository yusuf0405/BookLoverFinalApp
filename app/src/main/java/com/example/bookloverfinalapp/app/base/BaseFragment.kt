package com.example.bookloverfinalapp.app.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.domain.models.student.User
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.SAVED_STATE
import com.example.bookloverfinalapp.app.utils.dialog.LoadingDialog
import com.example.bookloverfinalapp.app.utils.extensions.showSnackbar
import com.example.bookloverfinalapp.app.utils.extensions.showToast
import com.example.bookloverfinalapp.app.utils.navigation.NavigationCommand
import com.example.bookloverfinalapp.app.utils.pref.CurrentUser

abstract class BaseFragment<V : ViewBinding, VM : BaseViewModel>(
    private val binder: (LayoutInflater, ViewGroup?, Boolean) -> V,
) : Fragment() {

    protected abstract val viewModel: VM

    private var viewBinding: V? = null

    protected fun binding(): V =
        checkNotNull(viewBinding)

    protected val currentUser: User by lazy(LazyThreadSafetyMode.NONE) {
        CurrentUser().getCurrentUser(activity = requireActivity())
    }

    protected abstract fun onReady(savedInstanceState: Bundle?)

    protected val loadingDialog: LoadingDialog by lazy(LazyThreadSafetyMode.NONE) {
        LoadingDialog(context = requireContext(), getString(R.string.loading_please_wait))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = binder.invoke(inflater, container, false)
        this.viewBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onReady(savedInstanceState)
        observeRecourse()
    }

    private fun observeRecourse() {
        viewModel.observeNavigation(viewLifecycleOwner) {
            it.getValue()?.let { navigationCommand ->
                handleNavigation(navigationCommand)
            }
        }
        viewModel.observeProgressDialog(viewLifecycleOwner) {
            Log.i("ffff","ffffffffff")
            it.getValue()?.let { status ->
                if (status) loadingDialog.show()
                else loadingDialog.dismiss()
            }
        }

        viewModel.observeNetworkError(viewLifecycleOwner) {
            it.getValue()?.let {
                showSnackbar(binding().root, getString(R.string.network_error))
            }
        }
        viewModel.observeError(viewLifecycleOwner) {
            it.getValue()?.let { message ->
                Log.i("dd","dddd")
                showToast(message = message)
            }
        }
    }

    private fun handleNavigation(navCommand: NavigationCommand) {
        when (navCommand) {
            is NavigationCommand.ToDirection -> findNavController().navigate(navCommand.directions)
            is NavigationCommand.Back -> findNavController().navigateUp()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SAVED_STATE, "SAVED_STATE")
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        viewBinding = null
        super.onDestroy()
    }
}