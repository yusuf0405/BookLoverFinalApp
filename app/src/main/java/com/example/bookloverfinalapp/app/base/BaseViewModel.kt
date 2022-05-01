package com.example.bookloverfinalapp.app.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.example.bookloverfinalapp.app.utils.Dispatchers
import com.example.bookloverfinalapp.app.utils.communication.*
import com.example.bookloverfinalapp.app.utils.event.Event
import com.example.bookloverfinalapp.app.utils.navigation.NavigationCommand

abstract class BaseViewModel : ViewModel() {
    private var progressCommunication = ProgressCommunication.Base()

    private var errorCommunication = ErrorCommunication.Base()

    private var networkErrorCommunication = NetworkErrorCommunication.Base()

    private var navigationCommunication = NavigationCommunication.Base()

    private var progressDialogCommunication = ProgressDialogCommunication.Base()

    var dispatchers = Dispatchers.Base()

    fun observeProgressAnimation(owner: LifecycleOwner, observer: Observer<Event<Boolean>>) =
        progressCommunication.observe(owner = owner, observer = observer)

    fun observeProgressDialog(owner: LifecycleOwner, observer: Observer<Event<Boolean>>) =
        progressDialogCommunication.observe(owner = owner, observer = observer)

    fun observeNavigation(owner: LifecycleOwner, observer: Observer<Event<NavigationCommand>>) =
        navigationCommunication.observe(owner = owner, observer = observer)

    fun observeError(owner: LifecycleOwner, observer: Observer<Event<String>>) =
        errorCommunication.observe(owner = owner, observer = observer)

    fun observeNetworkError(owner: LifecycleOwner, observer: Observer<Event<Boolean>>) =
        networkErrorCommunication.observe(owner = owner, observer = observer)

    fun navigate(navDirections: NavDirections) =
        navigationCommunication.map(Event(NavigationCommand.ToDirection(navDirections)))

    fun navigateBack() = navigationCommunication.map(Event(value = NavigationCommand.Back))

    fun error(message: String) = errorCommunication.map(Event(value = message))


    fun networkError() = networkErrorCommunication.map(Event(value = true))

    fun showProgressAnimation() = progressCommunication.map(Event(value = true))

    fun dismissProgressAnimation() = progressCommunication.map(Event(value = false))

    fun showProgressDialog() = progressDialogCommunication.map(source = Event(true))

    fun dismissProgressDialog() = progressDialogCommunication.map(source = Event(false))
}

