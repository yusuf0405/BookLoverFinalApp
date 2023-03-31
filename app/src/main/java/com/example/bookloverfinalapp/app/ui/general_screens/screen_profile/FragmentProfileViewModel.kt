package com.example.bookloverfinalapp.app.ui.general_screens.screen_profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserImage
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.bookloverfinalapp.app.utils.extensions.toImage
import com.example.data.ResourceProvider
import com.example.data.cache.models.IdResourceString
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.UpdateAnswerDomain
import com.example.domain.models.UserDomain
import com.example.domain.models.UserUpdateDomain
import com.example.domain.repository.UserCacheRepository
import com.example.domain.repository.UserRepository
import com.parse.ParseException
import com.parse.ParseFile
import com.parse.ProgressCallback
import com.parse.SaveCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FragmentProfileViewModel @Inject constructor(
    private val repository: UserRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val userCacheRepository: UserCacheRepository,
    private val resourceProvider: ResourceProvider,
    private val userDomainToUser: Mapper<UserDomain, User>,
    private val userToUserDomain: Mapper<User, UserDomain>
) : BaseViewModel(), SaveCallback, ProgressCallback {

    private var internalCurrentUserFlow = MutableStateFlow(User.unknown())

    private val _saveNewUserStatusFlow = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val saveNewUserStatusFlow get() = _saveNewUserStatusFlow.asSharedFlow()

    private val _startUpdateUserFlow = createMutableSharedFlowAsSingleLiveEvent<Unit>()
    val startUpdateUserFlow get() = _startUpdateUserFlow.asSharedFlow()

    private val _imageUploadDialogIsShowFlow = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val imageUploadDialogIsShowFlow get() = _imageUploadDialogIsShowFlow.asSharedFlow()

    private val _imageUploadDialogPercentFlow = createMutableSharedFlowAsSingleLiveEvent<Int>()
    val imageUploadDialogPercentFlow get() = _imageUploadDialogPercentFlow.asSharedFlow()

    private val _userSuccessUpdatedFlow =
        createMutableSharedFlowAsSingleLiveEvent<UpdateAnswerDomain>()
    val userSuccessUpdatedFlow get() = _userSuccessUpdatedFlow.asSharedFlow()

    private val _currentUserImageFile = MutableStateFlow<ParseFile?>(null)
    val currentUserImageFile get() = _currentUserImageFile.asStateFlow()

    private val _currentUserImage = MutableStateFlow<UserImage?>(null)
    val currentUserImage get() = _currentUserImage.asStateFlow()

    private val _motionPosition = MutableStateFlow(0f)
    val motionPosition get() = _motionPosition.asStateFlow()

    val currentUserFlow = userCacheRepository.fetchCurrentUserFromCacheFlow()
        .flowOn(Dispatchers.IO)
        .map(userDomainToUser::map)
        .onEach(internalCurrentUserFlow::emit)
        .stateIn(viewModelScope, SharingStarted.Lazily, User.unknown())

    fun updateUser(newUser: UserUpdateDomain) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { startUpdateUserParameters(newUser) },
            onSuccess = (_userSuccessUpdatedFlow::tryEmit),
            onError = { emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(it)) }
        )
    }

    fun checkParseFileAndStartUserUpdate() {
        if (currentUserImageFile.value == null) {
            _currentUserImage.tryEmit(getCurrentUser().image)
            _startUpdateUserFlow.tryEmit(Unit)
        } else {
            _imageUploadDialogIsShowFlow.tryEmit(true)
            saveImage()
        }
    }

    fun getCurrentUser() = internalCurrentUserFlow.value

    fun updateMotionPosition(position: Float) = _motionPosition.tryEmit(position)

    fun updateCurrentUserImageFile(file: ParseFile) = _currentUserImageFile.tryEmit(file)

    fun saveNewUserFromCache(user: User) = launchInBackground {
        val result = userCacheRepository.saveCurrentUserFromCache(userToUserDomain.map(user))
        if (user != User.unknown()) _saveNewUserStatusFlow.tryEmit(result)
        internalCurrentUserFlow.tryEmit(user)
    }

    private suspend fun startUpdateUserParameters(newUser: UserUpdateDomain) =
        repository.updateUserParameters(
            id = currentUserFlow.value.id,
            user = newUser,
            sessionToken = currentUserFlow.value.sessionToken
        )

    private fun saveImage() {
        currentUserImageFile.value?.apply {
            Log.i("Josephhh","parseFile = ${this}")
            saveInBackground((this@FragmentProfileViewModel), (this@FragmentProfileViewModel))
        }
    }

    private fun updateCurrentUserImage() {
        currentUserImageFile.value?.apply { _currentUserImage.tryEmit(toImage()) }
    }

    override fun done(parseException: ParseException?) {
        if (parseException == null) {
            updateCurrentUserImage()
            _imageUploadDialogIsShowFlow.tryEmit(false)
        } else {
            emitToErrorMessageFlow(IdResourceString(R.string.generic_error))
        }
    }

    override fun done(percentDone: Int?) {
        if (percentDone == null) return
        _imageUploadDialogPercentFlow.tryEmit(percentDone)
    }
}
