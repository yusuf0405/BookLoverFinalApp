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
import com.example.bookloverfinalapp.app.utils.navigation.NavigationCommand
import javax.inject.Inject


abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var progressCommunication: ProgressCommunication

    @Inject
    lateinit var errorCommunication: ErrorCommunication

    @Inject
    lateinit var navigationCommunication: NavigationCommunication

    @Inject
    lateinit var progressDialogCommunication: ProgressDialogCommunication

    @Inject
    lateinit var dispatchers: Dispatchers

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

    fun navigateBack() =
        launchInBackground { navigationCommunication.put(Event(value = NavigationCommand.Back)) }

    fun error(message: String) =
        launchInBackground { errorCommunication.put(Event(value = message)) }

    fun showProgressAnimation() =
        launchInBackground { progressCommunication.put(Event(value = true)) }

    fun dismissProgressAnimation() =
        launchInBackground { progressCommunication.put(Event(value = false)) }

    fun showProgressDialog() =
        launchInBackground { progressDialogCommunication.put(true) }

    fun dismissProgressDialog() =
        launchInBackground { progressDialogCommunication.put(false) }

    fun <T> launchInBackground(backgroundCall: suspend () -> T) =
        dispatchers.launchInBackground(viewModelScope) { backgroundCall() }
}

