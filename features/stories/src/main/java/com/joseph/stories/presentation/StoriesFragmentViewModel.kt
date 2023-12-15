package com.joseph.stories.presentation

import androidx.lifecycle.viewModelScope
import com.joseph.common.DispatchersProvider
import com.joseph.common.Mapper
import com.joseph.common.base.BaseViewModel
import com.joseph.stories.domain.usecases.FetchAllStoriesUseCase
import com.joseph.stories.presentation.mappers.StoriesToUserStoriesMapper
import com.joseph.stories.presentation.models.StoriesModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*


class StoriesFragmentViewModel @AssistedInject constructor(
    @Assisted private val storiesId: String,
    private val fetchAllStoriesUseCase: FetchAllStoriesUseCase,
    private val dispatchersProvider: DispatchersProvider,
    private val storiesToUserStoriesMapper: StoriesToUserStoriesMapper,
) : BaseViewModel() {

    private val storiesIdFlow = MutableStateFlow(storiesId)

    val stories = fetchAllStoriesUseCase()
        .flowOn(dispatchersProvider.io())
        .flowOn(dispatchersProvider.default())
        .map { storiesToUserStoriesMapper(it) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    @AssistedFactory
    interface Factory {

        fun create(
            storiesId: String
        ): StoriesFragmentViewModel
    }
}