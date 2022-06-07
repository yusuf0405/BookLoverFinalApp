package com.example.bookloverfinalapp.app.ui.general_screens.screen_edit_profile

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.domain.Status
import com.example.domain.models.UserUpdateDomain
import com.example.domain.interactor.UpdateUserUseCase
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
        user: UserUpdateDomain,
        sessionToken: String,
    ) = liveData(context = viewModelScope.coroutineContext) {
        updateUserUseCase.execute(id = id, user = user, sessionToken = sessionToken).collectLatest { resource ->
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