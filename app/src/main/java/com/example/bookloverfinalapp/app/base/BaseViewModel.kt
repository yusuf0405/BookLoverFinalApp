package com.example.bookloverfinalapp.app.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.example.bookloverfinalapp.app.utils.communication.ErrorCommunication
import com.example.bookloverfinalapp.app.utils.communication.NavigationCommunication
import com.example.bookloverfinalapp.app.utils.communication.ProgressCommunication
import com.example.bookloverfinalapp.app.utils.communication.ProgressDialogCommunication
import com.example.bookloverfinalapp.app.utils.dispatchers.Dispatchers
import com.example.bookloverfinalapp.app.utils.event.Event
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand
import com.example.bookloverfinalapp.app.utils.navigation.NavigationCommand
import com.example.data.cache.models.IdResourceString
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*


abstract class BaseViewModel : ViewModel() {

    private var progressCommunication = ProgressCommunication.Base()

    private var errorCommunication = ErrorCommunication.Base()

    private var navigationCommunication = NavigationCommunication.Base()

    private var progressDialogCommunication = ProgressDialogCommunication.Base()

    private var dispatchers = Dispatchers.Base()

    private var _navCommand = createMutableSharedFlowAsSingleLiveEvent<NavCommand>()
    val navCommand: SharedFlow<NavCommand> get() = _navCommand.asSharedFlow()

    private val _isErrorMessageIdFlow = createMutableSharedFlowAsSingleLiveEvent<IdResourceString>()
    val isErrorMessageIdFlow: SharedFlow<IdResourceString> get() = _isErrorMessageIdFlow.asSharedFlow()

    private val _isErrorMessageFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val isErrorMessageFlow: SharedFlow<String> get() = _isErrorMessageFlow.asSharedFlow()

    private val _showSuccessSnackbarFlow = createMutableSharedFlowAsSingleLiveEvent<IdResourceString>()
    val showSuccessSnackbarFlow get() = _showSuccessSnackbarFlow.asSharedFlow()

    fun <T> createMutableSharedFlowAsSingleLiveEvent(): MutableSharedFlow<T> =
        MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)

    fun <T> createMutableSharedFlowAsLiveData(): MutableSharedFlow<T> =
        MutableSharedFlow(1, 0, BufferOverflow.DROP_OLDEST)

    fun collectProgressAnimation(owner: LifecycleOwner, observer: Observer<Event<Boolean>>) =
        progressCommunication.observe(owner = owner, observer = observer)

    fun collectProgressDialog(owner: LifecycleOwner, observer: Observer<Boolean>) =
        progressDialogCommunication.observe(owner = owner, observer = observer)

    fun collectNavigation(owner: LifecycleOwner, observer: Observer<Event<NavigationCommand>>) =
        navigationCommunication.observe(owner = owner, observer = observer)

    fun collectError(owner: LifecycleOwner, observer: Observer<Event<String>>) =
        errorCommunication.observe(owner = owner, observer = observer)

    fun navigate(navDirections: NavDirections) =
        navigationCommunication.put(Event(NavigationCommand.ToDirection(navDirections)))

    fun emitToErrorMessageFlow(messageId: IdResourceString) =
        _isErrorMessageIdFlow.tryEmit(messageId)

    fun emitToShowSuccessNotificationFlow(messageId: IdResourceString) =
        _showSuccessSnackbarFlow.tryEmit(messageId)

    fun emitToErrorMessageFlow(message: String) = _isErrorMessageFlow.tryEmit(message)

    fun navigate(navCommand: NavCommand) = _navCommand.tryEmit(navCommand)

    fun navigateBack() =
        launchInBackground { navigationCommunication.put(Event(value = NavigationCommand.Back)) }

    fun error(message: String) =
        launchInBackground { errorCommunication.put(Event(value = message)) }

    fun showProgressAnimation() =
        launchInBackground { progressCommunication.put(Event(value = true)) }

    fun dismissProgressAnimation() =
        launchInBackground { progressCommunication.put(Event(value = false)) }


    fun <T> launchInBackground(backgroundCall: suspend () -> T) =
        dispatchers.launchInBackground(viewModelScope) { backgroundCall() }

    companion object {
        const val SEARCH_DEBOUNCE = 300L
    }
}

