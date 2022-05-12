package com.example.bookloverfinalapp.app.ui.screen_login

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.User
import com.example.domain.Mapper
import com.example.domain.interactor.SignInUseCase
import com.example.domain.models.UserDomain
import com.example.domain.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentLoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val mapper: Mapper<UserDomain, User>,
) : BaseViewModel() {

    fun signInWithEmailUseCase(email: String, password: String) =
        liveData(context = viewModelScope.coroutineContext) {
            signInUseCase.execute(email = email, password = password).collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
                    Status.SUCCESS -> {
                        dismissProgressDialog()
                        emit(mapper.map(resource.data!!))
                    }
                    Status.ERROR -> {
                        dismissProgressDialog()
                        error(message = resource.message!!)
                    }
                }
            }
        }

    fun goOverSignUpFragment() =
        navigate(FragmentLoginDirections.actionFragmentLoginToFragmentSignUp())

    fun goForgotPasswordFragment() =
        navigate(FragmentLoginDirections.actionFragmentLoginToFragmentForgotPassword())

    fun goBack() = navigateBack()

}