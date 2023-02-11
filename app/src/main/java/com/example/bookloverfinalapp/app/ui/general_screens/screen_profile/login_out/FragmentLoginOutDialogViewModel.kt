package com.example.bookloverfinalapp.app.ui.general_screens.screen_profile.login_out

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.App
import com.example.bookloverfinalapp.app.models.User
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.use_cases.ClearAllAppCacheUseCase
import com.example.domain.models.UserDomain
import com.example.domain.repository.UserCacheRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentLoginOutDialogViewModel @Inject constructor(
    private val clearAllAppCacheUseCase: ClearAllAppCacheUseCase,
    private val userCacheRepository: UserCacheRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val userToUserDomain: Mapper<User, UserDomain>
) : ViewModel() {

    fun loginOut() {
        clearDataInCache()
        saveUnknownUserFromCache()
    }

    private fun saveUnknownUserFromCache() = App.applicationScope.launch(dispatchersProvider.io()) {
        userCacheRepository.saveCurrentUserFromCache(userToUserDomain.map(User.unknown()))
    }


    private fun clearDataInCache() = App.applicationScope.launch(dispatchersProvider.io()) {
        clearAllAppCacheUseCase()
    }
}