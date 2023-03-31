package com.example.bookloverfinalapp.app.glue.stories

import com.example.domain.models.StoriesDomain
import com.example.domain.repository.StoriesRepository
import com.example.domain.Mapper
import com.joseph.stories.domain.repositories.StoriesFeatureRepository
import com.joseph.stories.presentation.models.StoriesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdapterStoriesRepository @Inject constructor(
    private val storiesRepository: StoriesRepository,
    private val storiesDomainToUiMapper: Mapper<StoriesDomain, StoriesModel>,
) : StoriesFeatureRepository {
    override fun fetchAllStoriesFromCloud(): Flow<List<StoriesModel>> =
        storiesRepository.fetchAllStoriesFromCache().map(::storiesMapTiUi)

    override fun fetchAllStoriesFromCache(): Flow<List<StoriesModel>> =
        storiesRepository.fetchAllStoriesFromCache().map(::storiesMapTiUi)

    private fun storiesMapTiUi(stories: List<StoriesDomain>) =
        stories.map(storiesDomainToUiMapper::map)

}