package com.example.bookloverfinalapp.app.ui.admin_screens.screen_profile

import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.domain.use_cases.ClearAllAppCacheUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentAdminProfileViewModel @Inject constructor(
    private val clearAllAppCacheUseCase: ClearAllAppCacheUseCase,
) : BaseViewModel() {

    fun clearDataInCache() {
        launchInBackground { clearAllAppCacheUseCase.invoke() }
    }

    fun goEditProfileFragment() =
        navigate(FragmentAdminProfileDirections.actionFragmentAdminProfileToFragmentAdminEditProfile())
}