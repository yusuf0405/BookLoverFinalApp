package com.example.bookloverfinalapp.app.ui.general_screens.screen_profile

import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.domain.interactor.ClearBooksCacheUseCase
import com.example.domain.interactor.ClearBooksThatReadCacheUseCase
import com.example.domain.interactor.ClearStudentsCacheUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentProfileViewModel @Inject constructor(
    private val clearBooksThatReadCacheUseCase: ClearBooksThatReadCacheUseCase,
    private val clearBooksCacheUseCase: ClearBooksCacheUseCase,
    private val clearStudentsCacheUseCase: ClearStudentsCacheUseCase,
) : BaseViewModel() {

    fun clearDataInCache() = launchInBackground {
        clearBooksCacheUseCase.execute()
        clearBooksThatReadCacheUseCase.execute()
        clearStudentsCacheUseCase.execute()
    }

    fun goEditProfileFragment() =
        navigate(FragmentProfileDirections.actionFragmentProfileToFragmentEditProfile())
}