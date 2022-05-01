package com.example.bookloverfinalapp.app.ui.general_screens.screen_login

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.User
import com.example.domain.domain.Mapper
import com.example.domain.models.Status
import com.example.domain.models.student.UserDomain
import com.example.domain.usecase.SignInUseCase
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
                    Status.NETWORK_ERROR -> {
                        dismissProgressDialog()
                        networkError()
                    }
                }
            }
        }

    fun goOverSignUpFragment() =
        navigate(FragmentLoginDirections.actionFragmentLoginToFragmentSignUp())

    fun goBack() = navigateBack()

}