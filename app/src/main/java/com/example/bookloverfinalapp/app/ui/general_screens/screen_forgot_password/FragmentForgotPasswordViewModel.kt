package com.example.bookloverfinalapp.app.ui.general_screens.screen_forgot_password

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.domain.Status
import com.example.domain.interactor.PasswordResetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentForgotPasswordViewModel @Inject constructor(
    private val passwordResetUseCase: PasswordResetUseCase,
) : BaseViewModel() {

    fun passwordReset(email: String) = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        passwordResetUseCase.execute(email = email).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> {
                    dismissProgressDialog()
                    emit(resource.data!!)
                }
                Status.ERROR -> {
                    dismissProgressDialog()
                    error(message = resource.message!!)
                }
            }
        }
    }

    fun goBack() = navigateBack()

}