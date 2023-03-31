package com.example.bookloverfinalapp.app.ui.general_screens.screen_splash

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.utils.extensions.dismissPlayerOverlay
import com.example.bookloverfinalapp.app.utils.extensions.showPlayerOverlay
import com.example.bookloverfinalapp.databinding.FragmentSplashBinding
import com.joseph.ui_core.extensions.launchOnLifecycle
import com.joseph.ui_core.extensions.launchWhenViewStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSplash :
    BaseFragment<FragmentSplashBinding, FragmentSplashViewModel>(FragmentSplashBinding::inflate) {

    override val viewModel: FragmentSplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()
        dismissPlayerOverlay()
        observeData()
    }

    private fun observeData() = with(viewModel) {
        launchOnLifecycle { isProgressBarVisibleFlow.observe(::setProgressBarVisible) }
        launchWhenViewStarted { navigateToFlow.observe(::navigateTo) }
    }

    private fun navigateTo(destination: StartNavigationDestination) {
        navControllerPopBackStackInclusive()
        when (destination) {
            StartNavigationDestination.NavigateToLoginScreen -> navigateToLoginScreens()
            StartNavigationDestination.NavigateToMainScreen -> navigateToMainScreens()
            StartNavigationDestination.NavigateToAdminScreen -> navigateToAdminScreens()
            StartNavigationDestination.NavigateToAccountHasDeletedScreen -> Unit
        }
    }

    private fun navControllerPopBackStackInclusive() =
        findNavController().popBackStack(R.id.splash_navigation, false)

    private fun navigateToLoginScreens() = findNavController().navigate(
        R.id.login_navigation,
        bundleOf(),
        createNavOptionsWithAnimations()
    )

    private fun navigateToMainScreens() = findNavController().navigate(
        R.id.main_navigation,
        bundleOf(),
        createNavOptionsWithAnimations()
    )

    private fun navigateToAdminScreens() = findNavController().navigate(
        R.id.admin_navigation,
        bundleOf(),
        createNavOptionsWithAnimations()
    )

    private fun createNavOptionsWithAnimations() = NavOptions
        .Builder()
        .setEnterAnim(R.anim.slide_up)
        .setExitAnim(R.anim.slide_down)
        .setPopEnterAnim(R.anim.slide_up)
        .setPopExitAnim(R.anim.slide_down)
        .build()

    private fun setProgressBarVisible(isVisible: Boolean) {
        binding().progressBar.isVisible = isVisible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showPlayerOverlay()
    }
}