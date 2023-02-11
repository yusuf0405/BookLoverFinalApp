package com.example.bookloverfinalapp.app.ui.admin_screens.screen_edit_profile

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.domain.models.UserUpdateDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentAdminEditProfileViewModel @Inject constructor(
) : BaseViewModel() {

    fun updateAdmin(
        id: String,
        user: UserUpdateDomain,
        sessionToken: String,
    ) = liveData(context = viewModelScope.coroutineContext) {
        emit(Unit)
//        updateUserUseCase.execute(id = id, user = user, sessionToken = sessionToken)
//            .collectLatest { resource ->
//                when (resource.status) {
//                    Status.LOADING -> showProgressDialog()
//                    Status.SUCCESS -> {
//                        dismissProgressDialog()
//                        emit(resource.data!!)
//                    }
//                    Status.ERROR -> {
//                        dismissProgressDialog()
//                        error(message = resource.message!!)
//                    }
//                    else -> Unit
//                }
            }


    fun goBack() = navigateBack()

}