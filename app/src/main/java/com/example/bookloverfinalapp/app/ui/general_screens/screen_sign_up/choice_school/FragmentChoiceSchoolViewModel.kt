package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up.choice_school

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserSignUp
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.SettingSelectionItem
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.data.ResourceProvider
import com.example.data.cache.models.IdResourceString
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.models.PostRequestAnswerDomain
import com.example.domain.models.SchoolDomain
import com.example.domain.models.UserDomain
import com.example.domain.repository.LoginRepository
import com.example.domain.repository.SchoolRepository
import com.example.domain.repository.UserCacheRepository
import com.example.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FragmentChoiceSchoolViewModel @Inject constructor(
    schoolRepository: SchoolRepository,
    private var loginRepository: LoginRepository,
    private val userCacheRepository: UserCacheRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider,
    private val mapper: Mapper<User, UserDomain>
) : BaseViewModel() {

    private val _isErrorMessageVisibleFlow = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val isErrorMessageVisibleFlow get() = _isErrorMessageVisibleFlow.asSharedFlow()

    private val _handleButtonOnClickFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val handleButtonOnClickFlow get() = _handleButtonOnClickFlow.asSharedFlow()

    private val _isProgressBarVisibleFlow = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val isProgressBarVisibleFlow get() = _isProgressBarVisibleFlow.asSharedFlow()

    private val _isProgressDialogVisibleFlow = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val isProgressDialogVisibleFlow get() = _isProgressDialogVisibleFlow.asSharedFlow()

    private val schoolsFlow = schoolRepository.getAllSchools()
        .onStart { emitIsProgressBarVisibleFlow(isVisible = true) }
        .onCompletion { emitIsProgressBarVisibleFlow(isVisible = false) }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.Lazily, Resource.loading())

    private val internalSchoolsFlow = schoolsFlow.map { it.data }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val settingSelectionFlow = internalSchoolsFlow.map(::mapToSettingSelectionItem)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _isShowErrorNotificationFlow =
        createMutableSharedFlowAsSingleLiveEvent<IdResourceString>()
    val isShowErrorNotificationFlow get() = _isShowErrorNotificationFlow.asSharedFlow()


    private val checkedItemFlow = MutableStateFlow(SchoolDomain.UNKNOWN)
    private var currentUserFlow = MutableStateFlow(User.unknown())

    private val _handleSignUpFlow = createMutableSharedFlowAsSingleLiveEvent<Unit>()
    val handleSignUpFlow get() = _handleSignUpFlow.asSharedFlow()

    fun navigateToChoiceClassFragment(user: UserSignUp) {
        if (checkedItemFlow.value == SchoolDomain.UNKNOWN) {
            emitErrorMessageVisibility(isVisible = true)
            return
        }
        emitErrorMessageVisibility(isVisible = false)
        user.schoolId = checkedItemFlow.value.id
        user.schoolName = checkedItemFlow.value.title
        _handleButtonOnClickFlow.tryEmit(checkedItemFlow.value.id)
    }

    fun startUserSignUp(user: UserSignUp) {
        emitIsProgressDialogVisibleFlow(isVisible = true)
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { loginRepository.signUp(user = user.mapToUserSignUpDomain()) },
            onSuccess = {
                emitIsProgressDialogVisibleFlow(isVisible = false)
                setAndMapToCurrentUser(it, user)
                startAddingSessionToken(it)
            },
            onError = {
                handleError(it)
                emitIsProgressDialogVisibleFlow(isVisible = false)
            }

        )
    }

    fun setItemOnClickListener(id: String) {
        internalSchoolsFlow.value?.map { if (it.id == id) checkedItemFlow.tryEmit(it) }
    }

    private fun handleError(exception: Throwable) {
        val errorMessage = resourceProvider.fetchIdErrorMessage(exception)
        emitIsShowErrorNotificationFlow(errorMessage)
    }

    private fun setAndMapToCurrentUser(
        requestAnswerDomain: PostRequestAnswerDomain,
        userSignUp: UserSignUp
    ) {
        val newUser = userSignUp.mapToUser(
            id = requestAnswerDomain.id,
            sessionToken = requestAnswerDomain.sessionToken,
            createdAt = requestAnswerDomain.createdAt,
            image = requestAnswerDomain.image
        )
        currentUserFlow.tryEmit(newUser)
    }

    private fun startAddingSessionToken(user: PostRequestAnswerDomain) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { userRepository.addSessionToken(user.id, user.sessionToken) },
            onSuccess = {
                saveNewCurrentUserToCache()
                _handleSignUpFlow.tryEmit(Unit)
            },
            onError = {
                handleAddingSessionTokenErrorResult()
            }
        )
    }

    private fun handleAddingSessionTokenErrorResult() {
        emitIsShowErrorNotificationFlow(IdResourceString(R.string.generic_error))
    }

    private fun saveNewCurrentUserToCache() = launchInBackground {
        userCacheRepository.saveCurrentUserFromCache(mapper.map(currentUserFlow.value))
    }

    private fun mapToSettingSelectionItem(schools: List<SchoolDomain>?) =
        schools?.map { SettingSelectionItem(id = it.id, it.title, isChecked = false) }

    private fun emitErrorMessageVisibility(isVisible: Boolean) {
        _isErrorMessageVisibleFlow.tryEmit(isVisible)
    }

    private fun emitIsProgressBarVisibleFlow(isVisible: Boolean) {
        _isProgressBarVisibleFlow.tryEmit(isVisible)
    }

    private fun emitIsProgressDialogVisibleFlow(isVisible: Boolean) {
        _isProgressDialogVisibleFlow.tryEmit(isVisible)
    }

    private fun emitIsShowErrorNotificationFlow(message: IdResourceString) {
        _isShowErrorNotificationFlow.tryEmit(message)
    }
}

