package com.example.bookloverfinalapp.app.ui.screen_forgot_password

import android.util.Log
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.utils.extensions.viewModelScope
import com.example.domain.domain.interactor.PasswordResetUseCase
import com.example.domain.models.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
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
                    emit(resource.data!!)
                    dismissProgressDialog()
                }
                Status.ERROR -> {
                    error(message = resource.message!!)
                    dismissProgressDialog()
                }
            }
        }
    }

    fun goBack() = navigateBack()

}