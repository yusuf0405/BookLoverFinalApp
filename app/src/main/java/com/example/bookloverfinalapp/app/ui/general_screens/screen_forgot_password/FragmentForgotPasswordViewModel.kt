package com.example.bookloverfinalapp.app.ui.general_screens.screen_forgot_password

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.domain.DispatchersProvider
import com.example.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentForgotPasswordViewModel @Inject constructor(
    private var loginRepository: LoginRepository,
    private val dispatchersProvider: DispatchersProvider,
) : BaseViewModel() {

    fun passwordReset(email: String) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { loginRepository.passwordReset(email) },
            onSuccess = {},
            onError = {}
        )
    }

}