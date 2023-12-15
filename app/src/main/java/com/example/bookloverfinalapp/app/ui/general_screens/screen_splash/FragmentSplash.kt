package com.example.bookloverfinalapp.app.ui.general_screens.screen_splash

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.utils.extensions.dismissPlayerOverlay
import com.example.bookloverfinalapp.app.utils.extensions.showPlayerOverlay
import com.example.bookloverfinalapp.databinding.FragmentSplashBinding
import com.joseph.ui.core.extensions.launchOnLifecycle
import com.joseph.ui.core.extensions.launchWhenViewStarted
import dagger.hilt.android.AndroidEntryPoint
import com.example.bookloverfinalapp.R as MainRes

@AndroidEntryPoint
class FragmentSplash :
    BaseFragment<FragmentSplashBinding, FragmentSplashViewModel>(FragmentSplashBinding::inflate) {

    override val viewModel: FragmentSplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()
        dismissPlayerOverlay()
        observeData()
        binding().lottieAnimationView.isAnimating
    }

    private fun observeData() = with(viewModel) {
        binding().lottieAnimationView.isInvisible
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
        findNavController().popBackStack(MainRes.id.splash_navigation, false)

    private fun navigateToLoginScreens() = findNavController().navigate(
        MainRes.id.login_navigation,
        bundleOf(),
        createNavOptionsWithAnimations()
    )

    private fun navigateToMainScreens() = findNavController().navigate(
        MainRes.id.main_navigation,
        bundleOf(),
        createNavOptionsWithAnimations()
    )

    private fun navigateToAdminScreens() = findNavController().navigate(
        MainRes.id.admin_navigation,
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