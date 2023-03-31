package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up.choice_class

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserSignUp
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.SettingSelectionItem
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.data.ResourceProvider
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.ClassDomain
import com.example.domain.models.PostRequestAnswerDomain
import com.example.domain.models.UserDomain
import com.example.domain.models.UserSignUpDomain
import com.example.domain.repository.ClassRepository
import com.example.domain.repository.LoginRepository
import com.example.domain.repository.UserCacheRepository
import com.example.domain.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class FragmentChoiceClassViewModel @AssistedInject constructor(
    @Assisted private val schoolId: String,
    @Assisted private val userSignUp: UserSignUp,
    private val repository: ClassRepository,
    private var loginRepository: LoginRepository,
    private val userRepository: UserRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val userCacheRepository: UserCacheRepository,
    private val resourceProvider: ResourceProvider,
    private val mapper: Mapper<User, UserDomain>
) : BaseViewModel() {

    private val internalClassesFlow = repository.fetchAllClassCloud(schoolId = schoolId)
        .onStart { emitIsProgressBarVisibleFlow(isVisible = true) }
        .onCompletion { emitIsProgressBarVisibleFlow(isVisible = false) }
        .catch { exception: Throwable -> handleError(exception) }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    val settingSelectionFlow = internalClassesFlow.map(::mapToSettingSelectionItem)
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    private val _isProgressBarVisibleFlow = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val isProgressBarVisibleFlow get() = _isProgressBarVisibleFlow.asSharedFlow()

    private val _isProgressDialogVisibleFlow = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val isProgressDialogVisibleFlow get() = _isProgressDialogVisibleFlow.asSharedFlow()

    private val _isErrorMessageVisibleFlow = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val isErrorMessageVisibleFlow get() = _isErrorMessageVisibleFlow.asSharedFlow()

    private val _handleSignUpFlow = createMutableSharedFlowAsSingleLiveEvent<Unit>()
    val handleSignUpFlow get() = _handleSignUpFlow.asSharedFlow()

    private var checkedItemFlow = MutableStateFlow(ClassDomain.unknown())
    private var currentUserFlow = MutableStateFlow(User.unknown())

    fun startSignUp() {
        if (checkedItemFlow.value == ClassDomain.unknown()) {
            emitIsErrorMessageVisibleFlow(isVisible = true)
            return
        }
        emitIsErrorMessageVisibleFlow(isVisible = false)
        userSignUp.classId = checkedItemFlow.value.id
        userSignUp.className = checkedItemFlow.value.title
        signUpUser(userSignUp.mapToUserSignUpDomain())
    }

    fun handleItemOnClickListener(id: String) {
        internalClassesFlow.value.forEach { if (it.id == id) checkedItemFlow.tryEmit(it) }
    }

    private fun signUpUser(user: UserSignUpDomain) {
        emitIsProgressDialogVisibleFlow(isVisible = true)
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { loginRepository.signUp(user = user) },
            onSuccess = {
                emitIsProgressDialogVisibleFlow(isVisible = false)
                setAndMapToCurrentUser(it)
                startAddingSessionToken()
            },
            onError = {
                handleError(it)
                emitIsProgressDialogVisibleFlow(isVisible = false)
            }

        )
    }

    private fun startAddingSessionToken() {
        val user = currentUserFlow.value
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { userRepository.addSessionToken(user.id, user.sessionToken) },
            onError = ::handleError,
            onSuccess = { handleAddingSessionTokenResult() }
        )
    }

    private fun handleAddingSessionTokenResult() {
        saveNewCurrentUserToCache()
        _handleSignUpFlow.tryEmit(Unit)
    }

    private fun saveNewCurrentUserToCache() = launchInBackground {
        userCacheRepository.saveCurrentUserFromCache(mapper.map(currentUserFlow.value))
    }

    private fun handleError(exception: Throwable) {
        val errorMessage = resourceProvider.fetchIdErrorMessage(exception)
        emitToErrorMessageFlow(errorMessage)
    }

    private fun setAndMapToCurrentUser(requestAnswerDomain: PostRequestAnswerDomain) {
        val newUser = userSignUp.mapToUser(
            id = requestAnswerDomain.id,
            sessionToken = requestAnswerDomain.sessionToken,
            createdAt = requestAnswerDomain.createdAt,
            image = requestAnswerDomain.image
        )
        currentUserFlow.tryEmit(newUser)
    }

    private fun mapToSettingSelectionItem(classes: List<ClassDomain>) =
        classes.map { SettingSelectionItem(id = it.id, it.title, isChecked = false) }

    private fun emitIsProgressBarVisibleFlow(isVisible: Boolean) {
        _isProgressBarVisibleFlow.tryEmit(isVisible)
    }

    private fun emitIsProgressDialogVisibleFlow(isVisible: Boolean) {
        _isProgressDialogVisibleFlow.tryEmit(isVisible)
    }

    private fun emitIsErrorMessageVisibleFlow(isVisible: Boolean) {
        _isErrorMessageVisibleFlow.tryEmit(isVisible)
    }

    @AssistedFactory
    interface Factory {

        fun create(
            schoolId: String,
            userSignUp: UserSignUp
        ): FragmentChoiceClassViewModel
    }
}