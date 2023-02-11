package com.example.bookloverfinalapp.app.ui.general_screens.screen_user_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val USER_ID_KEY = "USER_ID_KEY"


class FragmentUserInfoDialogViewModelFactory @AssistedInject constructor(
    @Assisted(USER_ID_KEY) private val userId: String,
    private val userRepository: UserRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == FragmentUserInfoViewModel::class.java)
        return FragmentUserInfoViewModel(
            userId = userId,
            userRepository = userRepository,
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(USER_ID_KEY) userId: String,
        ): FragmentUserInfoDialogViewModelFactory
    }
}