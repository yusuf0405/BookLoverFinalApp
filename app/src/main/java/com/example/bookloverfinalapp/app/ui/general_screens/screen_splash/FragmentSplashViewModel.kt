package com.example.bookloverfinalapp.app.ui.general_screens.screen_splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserType
import com.example.bookloverfinalapp.app.utils.FetchInternetConnectedStatus
import com.example.domain.Mapper
import com.example.domain.use_cases.*
import com.example.domain.models.UserDomain
import com.example.domain.repository.ClassRepository
import com.example.domain.repository.UserCacheRepository
import com.example.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FragmentSplashViewModel @Inject constructor(
    fetchInternetConnectedStatus: FetchInternetConnectedStatus,
    clearAllAppCacheUseCase: ClearAllAppCacheUseCase,
    private val classRepository: ClassRepository,
    private val userRepository: UserRepository,
    private val userCacheRepository: UserCacheRepository,
    private val mapper: Mapper<UserDomain, User>
) : BaseViewModel() {

    init {
        launchInBackground {
            clearAllAppCacheUseCase()
        }
    }

    private val _isProgressBarVisibleFlow = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val isProgressBarVisibleFlow get() = _isProgressBarVisibleFlow.asSharedFlow()

    private val _navigateToFlow =
        createMutableSharedFlowAsSingleLiveEvent<StartNavigationDestination>()
    val navigateToFlow get() = _navigateToFlow.asSharedFlow()

    private val isNetworkConnectedFlow = fetchInternetConnectedStatus()
        .flowOn(Dispatchers.IO)
        .shareIn(viewModelScope, SharingStarted.Lazily, 1)

    private val currentUserFromCacheFlow = userCacheRepository.fetchCurrentUserFromCacheFlow()
        .flowOn(Dispatchers.IO)
        .map(mapper::map)
        .filterNotNull()
        .flowOn(Dispatchers.Default)
        .onEach(::handleCurrentUserFromCache)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val userClassesFlow = currentUserFromCacheFlow.filterNotNull().flatMapLatest {
        classRepository.fetchUserClassesFromId(it.id)
    }.flowOn(Dispatchers.IO)
        .onEmpty { emitNavigateToFlow(StartNavigationDestination.NavigateToAccountHasDeletedScreen) }
        .onStart { showProgressBar() }
        .onEach { emitNavigateToFlow(StartNavigationDestination.NavigateToMainScreen) }
        .catch { error: Throwable -> handleError(error) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val navigateActions: Map<UserType, StartNavigationDestination> by lazy {
        mapOf(
            UserType.unknown to StartNavigationDestination.NavigateToLoginScreen,
            UserType.admin to StartNavigationDestination.NavigateToAdminScreen,
            UserType.teacher to StartNavigationDestination.NavigateToMainScreen,
        )
    }

    init {
        currentUserFromCacheFlow.launchIn(viewModelScope)
    }

    private fun fetchCurrentUserFromCloud(sessionToken: String) {
        isNetworkConnectedFlow
            .filter { it }
            .flatMapLatest { userRepository.getCurrentUserWithCloud(sessionToken) }
            .flowOn(Dispatchers.IO)
            .onStart { showProgressBar() }
            .onEach { userClassesFlow.launchIn(viewModelScope) }
            .catch { error: Throwable -> handleError(error) }
            .flowOn(Dispatchers.Default)
            .onEach(userCacheRepository::saveCurrentUserFromCache)
            .launchIn(viewModelScope)
    }

    private fun handleCurrentUserFromCache(user: User) = launchInBackground {
        withTimeout(SPLASH_SCREEN_DEFAULT_DELAY_TIME) {
            delay(1000)
            _navigateToFlow.tryEmit(searchNavigateInUserType(user.userType))
//            when (user.userType) {
//                UserType.unknown -> {
//                    delay(SPLASH_SCREEN_DEFAULT_DELAY_TIME)
//                    emitNavigateToFlow(StartNavigationDestination.NavigateToLoginScreen)
//                }
//                UserType.admin -> {
//                    delay(SPLASH_SCREEN_DEFAULT_DELAY_TIME)
//                    emitNavigateToFlow(StartNavigationDestination.NavigateToAdminScreen)
//                }
//                else -> {
//                    fetchCurrentUserFromCloud(user.sessionToken)
//                }
//            }
        }
    }

    private fun searchNavigateInUserType(userType: UserType) =
        navigateActions[userType] ?: StartNavigationDestination.NavigateToMainScreen

    private fun handleError(error: Throwable) {
        Log.i("Joseph", error.toString())
        currentUserFromCacheFlow.value?.apply {
            _navigateToFlow.tryEmit(searchNavigateInUserType(userType))
        }
//        handleCurrentUserFromCache(currentUserFromCacheFlow.value ?: User.unknown())
    }

    private fun emitNavigateToFlow(destination: StartNavigationDestination) {
        _navigateToFlow.tryEmit(destination)
    }

    private fun showProgressBar() {
        _isProgressBarVisibleFlow.tryEmit(true)
    }

    private companion object {
        const val SPLASH_SCREEN_DEFAULT_DELAY_TIME = 3000L
    }
}