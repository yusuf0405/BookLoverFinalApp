package com.joseph.profile.presentation

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.joseph.profile.domain.models.UserFeatureModel
import com.joseph.profile.presentation.screen_edit_profile.EditProfileViewModel
import com.joseph.profile.presentation.screen_profile.FragmentProfileScreenViewModel
import com.joseph.ui.core.MyApplicationTheme
import com.joseph.ui.core.R
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.core.assistedViewModel
import com.joseph.core.extensions.dataStore
import com.joseph.core.extensions.getAttrColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

const val KEY_APP_MODE = "KEY_APP_MODE"

@AndroidEntryPoint
class FragmentProfileScreen : Fragment() {

    private val viewModel by viewModels<FragmentProfileScreenViewModel>()

    @Inject
    lateinit var factory: EditProfileViewModel.Factory
    private val editProfileViewModel: EditProfileViewModel by assistedViewModel {
        factory.create(UserFeatureModel.unknown())
    }

    private val isNightMode: Boolean by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK ==
                Configuration.UI_MODE_NIGHT_YES
    }

    override fun onStart() {
        super.onStart()
        requireActivity().apply {
            window.statusBarColor = getAttrColor(R.attr.card_background)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            MyApplicationTheme(
                darkTheme = isSystemInDarkTheme()
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    ProfileScreensApp(
                        profileScreenViewModel = viewModel,
                        isDarkTheme = isSystemInDarkTheme(),
                        onThemeChange = ::setAppTheme,
                        navigateSettingScreen = ::navigateSettingScreen,
                        navigateLoginOutScreen = ::navigateLoginOutScreen,
                        editProfileViewModel = editProfileViewModel,
                    )
                }
            }

        }
    }

    private fun navigateSettingScreen() = viewModel.fetchSettingDialog()
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun navigateLoginOutScreen() = viewModel.fetchLoginOutDialog()
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun setAppTheme(isDarkTheme: Boolean) {
        lifecycleScope.launch {
            if (isDarkTheme == isNightMode) return@launch
            requireContext().dataStore.edit { pref ->
                pref[booleanPreferencesKey(KEY_APP_MODE)] = isDarkTheme
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.statusBarColor = Color.TRANSPARENT
    }
}