package com.example.bookloverfinalapp.app.ui.student_screens.screen_progress

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Status
import com.example.domain.usecase.GetStudentProgressUseCase
import com.example.bookloverfinalapp.app.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentStudentProgressViewModel @Inject constructor(
    private val getStudentProgressUseCase: GetStudentProgressUseCase,
) : BaseViewModel() {

    fun getMyProgress(id: String) = liveData(context = viewModelScope.coroutineContext) {
        getStudentProgressUseCase.execute(id = id).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressAnimation()
                Status.SUCCESS -> {
                    emit(resource.data!!)
                    dismissProgressAnimation()
                }
                Status.ERROR -> {
                    error(message = resource.message!!)
                    dismissProgressAnimation()
                }
                Status.NETWORK_ERROR -> {
                    networkError()
                    dismissProgressAnimation()
                }
            }
        }
    }

    fun go() =
        navigate(FragmentStudentProgressDirections.actionFragmentStudentProgressToFragmentAdminUploadPdf())
}