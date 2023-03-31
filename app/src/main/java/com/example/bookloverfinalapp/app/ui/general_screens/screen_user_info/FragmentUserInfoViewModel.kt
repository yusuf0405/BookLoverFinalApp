package com.example.bookloverfinalapp.app.ui.general_screens.screen_user_info

import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookItemOnClickListeners
import com.example.bookloverfinalapp.app.utils.extensions.createSharedFlowAsSingleLiveEvent
import com.example.data.cache.models.IdResourceString
import com.example.domain.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map

class FragmentUserInfoViewModel @AssistedInject constructor(
    @Assisted userId: String,
    private val userRepository: UserRepository,
) : BaseViewModel(), SavedBookItemOnClickListeners {

    private val userIdFlow = MutableStateFlow(userId)

    private val _isErrorFlow = createSharedFlowAsSingleLiveEvent<IdResourceString>()
    val isErrorFlow: SharedFlow<IdResourceString> get() = _isErrorFlow.asSharedFlow()

    val userFlow = userIdFlow.map(userRepository::fetchUserFromId)

    @AssistedFactory
    interface Factory {
        fun create(
            userId: String,
        ): FragmentUserInfoViewModel
    }
}