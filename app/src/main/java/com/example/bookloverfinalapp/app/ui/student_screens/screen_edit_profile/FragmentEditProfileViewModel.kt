package com.example.bookloverfinalapp.app.ui.student_screens.screen_edit_profile

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Status
import com.example.domain.models.student.StudentUpdateRes
import com.example.domain.usecase.UpdateUserUseCase
import com.example.bookloverfinalapp.app.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentEditProfileViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
) : BaseViewModel() {

    fun updateStudent(
        id: String,
        student: StudentUpdateRes,
        sessionToken: String,
    ) = liveData(context = viewModelScope.coroutineContext) {
        updateUserUseCase.execute(id = id, student = student, sessionToken = sessionToken)
            .collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
                    Status.SUCCESS -> {
                        emit(resource.data!!)
                        dismissProgressDialog()
                    }
                    Status.ERROR -> {
                        error(message = resource.message!!)
                        dismissProgressDialog()
                    }
                    Status.NETWORK_ERROR -> {
                        networkError()
                        dismissProgressDialog()
                    }
                }

            }
    }

    fun goBack() = navigateBack()
}