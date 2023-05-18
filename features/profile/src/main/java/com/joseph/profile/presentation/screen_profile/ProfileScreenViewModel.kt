package com.joseph.profile.presentation.screen_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.profile.domain.models.UserFeatureModel
import com.joseph.profile.domain.usecases.FetchLoginOutDialogUseCase
import com.joseph.profile.domain.usecases.FetchUserInfoUseCase
import com.joseph.profile.domain.usecases.FetchSettingDialogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileScreenUiState(
    val loading: Boolean = false,
    val errorMessage: String = String(),
    val user: UserFeatureModel = UserFeatureModel.unknown()
)

@HiltViewModel
class FragmentProfileScreenViewModel @Inject constructor(
    private val fetchUserInfoUseCase: FetchUserInfoUseCase,
    private val fetchSettingDialogUseCase: FetchSettingDialogUseCase,
    private val fetchLoginOutDialogUseCase: FetchLoginOutDialogUseCase
) : ViewModel() {

    var uiState by mutableStateOf(ProfileScreenUiState())

    init {
        fetchUserInfo()
    }

    fun fetchSettingDialog() = fetchSettingDialogUseCase()

    fun fetchLoginOutDialog() = fetchLoginOutDialogUseCase()

    private fun fetchUserInfo() {
        uiState = uiState.copy(loading = true)
        viewModelScope.launch {
            runCatching {
                fetchUserInfoUseCase()
            }.onSuccess { user ->
                uiState = uiState.copy(loading = false, user = user)
            }.onFailure {
                uiState = uiState.copy(loading = false, errorMessage = "Ошибка")
            }
        }
    }
}