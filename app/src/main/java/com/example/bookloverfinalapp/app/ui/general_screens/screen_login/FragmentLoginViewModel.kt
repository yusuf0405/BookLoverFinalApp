package com.example.bookloverfinalapp.app.ui.general_screens.screen_login

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.UserDomain
import com.example.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class FragmentLoginViewModel @Inject constructor(
    private var repository: LoginRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val mapper: Mapper<UserDomain, User>,
) : BaseViewModel() {

    private val _successSignInFlow = createMutableSharedFlowAsSingleLiveEvent<User>()
    val successSignInFlow get() = _successSignInFlow.asSharedFlow()

    fun signInWithEmailUseCase(email: String, password: String) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { repository.signIn(email, password) },
            onSuccess = { _successSignInFlow.tryEmit(mapper.map(it)) },
            onError = {}
        )
    }

    fun goOverSignUpFragment() =
        navigate(FragmentLoginDirections.actionFragmentLoginToFragmentSelectSignUp())

    fun goForgotPasswordFragment() =
        navigate(FragmentLoginDirections.actionFragmentLoginToFragmentForgotPassword())
}