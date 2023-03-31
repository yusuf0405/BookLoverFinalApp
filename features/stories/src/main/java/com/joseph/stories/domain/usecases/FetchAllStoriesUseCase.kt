package com.joseph.stories.domain.usecases

import com.joseph.common_api.CheckInternetConnection
import com.joseph.common_api.EmptyListException
import com.joseph.stories.domain.repositories.StoriesFeatureRepository
import com.joseph.stories.presentation.models.StoriesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

interface FetchAllStoriesUseCase {

    operator fun invoke(): Flow<List<StoriesModel>>

}

class FetchAllStoriesUseCaseImpl @Inject constructor(
    private val repository: StoriesFeatureRepository,
    private val checkInternetConnection: CheckInternetConnection
) : FetchAllStoriesUseCase {

    override fun invoke(): Flow<List<StoriesModel>> =
        if (checkInternetConnection.isOnline()) {
            repository.fetchAllStoriesFromCloud()
        } else {
            repository.fetchAllStoriesFromCache()
        }.onEach { stories ->
            if (stories.isEmpty()) throw EmptyListException()
        }
}