package com.joseph.utils_core

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Action to be executed when for create view model
 */
internal typealias ViewModelCreator<VM> = () -> VM

/**
 * Factory for creating instances ([ViewModel]) from assisted injection
 * @param viewModelCreator action for create view model
 */
class AssistedViewModelFactory<VM : ViewModel>(
    private val viewModelCreator: ViewModelCreator<VM>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = viewModelCreator()
        require(modelClass == viewModel::class.java)
        return viewModel as T
    }
}

/**
 * Extension function for create view model in ([Fragment]) with the help assisted injection
 * @param creator action for create view model
 */
inline fun <reified VM : ViewModel> Fragment.assistedViewModel(
    noinline creator: ViewModelCreator<VM>
): Lazy<VM> = viewModels(
    factoryProducer = { AssistedViewModelFactory(creator) }
)