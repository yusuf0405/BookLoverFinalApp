package com.example.bookloverfinalapp.app.ui.general_screens.screen_profile.choice_image_dialog

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.HeaderItem
import com.example.bookloverfinalapp.app.utils.extensions.createSharedFlowAsLiveData
import com.example.data.cache.models.IdResourceString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FragmentChoiceImageViewModel @Inject constructor(

) : ViewModel(), DefaultAvatarItemOnClickListener {

    val adapterItems = flow {
        emit(createAdapterItems())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _choiceImageResourceFlow = MutableStateFlow<Int?>(null)
    val choiceImageResourceFlow = _choiceImageResourceFlow.asStateFlow()

    private fun createAdapterItems() = listOf(
        createHeader(R.string.default_avatars),
        createDefaultAvatar(resource = R.drawable.avatar_01),
        createDefaultAvatar(resource = R.drawable.avatar_06),
        createDefaultAvatar(resource = R.drawable.avatar_12),
        createDefaultAvatar(resource = R.drawable.avatar_13),
        createDefaultAvatar(resource = R.drawable.avatar_18),
        createDefaultAvatar(resource = R.drawable.avatar_19),
        createDefaultAvatar(resource = R.drawable.avatar_20),
        createDefaultAvatar(resource = R.drawable.avatar_21),
        createHeader(R.string.other),
    )

    private fun createDefaultAvatar(@DrawableRes resource: Int) = DefaultAvatarItem(
        imageResource = resource,
        listener = this
    )

    private fun createHeader(@StringRes resource: Int) = HeaderItem(
        onClickListener = {},
        titleId = IdResourceString(resource)
    )

    fun clearChoiceImageResourceFlow() = _choiceImageResourceFlow.tryEmit(null)

    override fun defaultAvatarOnClickListener(imageResource: Int) {
        _choiceImageResourceFlow.tryEmit(imageResource)
    }

}