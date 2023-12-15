package com.example.bookloverfinalapp.app.ui.general_screens.screen_login

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.data.ResourceProvider
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.UserDomain
import com.example.domain.repository.LoginRepository
import com.example.domain.repository.UserCacheRepository
import com.joseph.profile.domain.repositories.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentLoginViewModel @Inject constructor(
    private var repository: LoginRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val resourceProvider: ResourceProvider,
    private val userInfoRepository: UserCacheRepository,
    private val mapper: Mapper<UserDomain, User>,
) : BaseViewModel() {

    private val _successSignInFlow = createMutableSharedFlowAsSingleLiveEvent<User>()
    val successSignInFlow get() = _successSignInFlow.asSharedFlow()

    private val _loadingDialogFlow = MutableStateFlow<Boolean?>(null)
    val loadingDialogFlow get() = _loadingDialogFlow.asStateFlow()

    fun signInWithEmailUseCase(email: String, password: String) {
        showLoadingDialog()
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { repository.signIn(email, password) },
            onStart = { showLoadingDialog() },
            onSuccess = {
                viewModelScope.launch {
                    userInfoRepository.saveCurrentUserFromCache(it)
                }
                _successSignInFlow.tryEmit(mapper.map(it))
                dismissLoadingDialog()
            },
            onError = {
                emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(it))
                dismissLoadingDialog()
            }
        )
    }

    fun goOverSignUpFragment() =
        navigate(FragmentLoginDirections.actionFragmentLoginToFragmentSelectSignUp())

    fun goForgotPasswordFragment() =
        navigate(FragmentLoginDirections.actionFragmentLoginToFragmentForgotPassword())

    private fun showLoadingDialog() = _loadingDialogFlow.tryEmit(true)

    private fun dismissLoadingDialog() = _loadingDialogFlow.tryEmit(false)
}