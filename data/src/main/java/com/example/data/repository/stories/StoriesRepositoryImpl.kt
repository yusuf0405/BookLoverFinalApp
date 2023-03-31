package com.example.data.repository.stories

import com.example.data.cache.source.stories.StoriesCacheDataSource
import com.example.data.cloud.models.AddStoriesCloud
import com.example.data.cloud.models.PostRequestAnswerCloud
import com.example.data.cloud.source.stories.StoriesCloudDataSource
import com.example.data.mapToBook
import com.example.data.models.AddStoriesData
import com.example.data.models.StoriesData
import com.example.data.repository.BaseRepository
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.RequestState
import com.example.domain.models.AddStoriesDomain
import com.example.domain.models.AudioBookDomain
import com.example.domain.models.StoriesDomain
import com.example.domain.repository.StoriesRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor(
    private val cacheDataSource: StoriesCacheDataSource,
    private val cloudDataSource: StoriesCloudDataSource,
    private val dispatchersProvider: DispatchersProvider,
    private val storiesDataToDomainMapper: Mapper<StoriesData, StoriesDomain>,
    private val addStoriesDomainToDataMapper: Mapper<AddStoriesDomain, AddStoriesData>,
    private val addStoriesDataToCloudMapper: Mapper<AddStoriesData, AddStoriesCloud>
) : StoriesRepository, BaseRepository {

    override fun fetchAllStories(schoolId: String): Flow<List<StoriesDomain>> = flow {
        emit(cacheDataSource.fetchAllStoriesFromCacheSingle())
    }.flatMapLatest { handleFetchBooksInCache(it, schoolId) }
        .map { audioBooks -> audioBooks.map(storiesDataToDomainMapper::map) }
        .flowOn(dispatchersProvider.default())

    override fun fetchAllStoriesFromCache(): Flow<List<StoriesDomain>> =
        cacheDataSource.fetchAllStoriesFromCacheObservable()
            .map { audioBooks -> audioBooks.map(storiesDataToDomainMapper::map) }
            .flowOn(dispatchersProvider.default())

    private fun handleFetchBooksInCache(
        cachedStories: List<StoriesData>,
        schoolId: String
    ) = if (cachedStories.isEmpty()) cloudDataSource.fetchAllStoriesFromCloud(schoolId = schoolId)
        .onEach { books -> books.forEach { cacheDataSource.saveNewStoriesToCache(it) } }
    else cacheDataSource.fetchAllStoriesFromCacheObservable()

    override suspend fun fetchStoriesFromCache(storiesId: String): StoriesDomain {
        val audioBook = cacheDataSource.fetchStoriesFromId(storiesId = storiesId)
        return storiesDataToDomainMapper.map(audioBook)
    }

    override suspend fun addNewStories(newStories: AddStoriesDomain): RequestState<Unit> =
        renderResultToUnit(
            result = cloudDataSource.addNewStories(
                addNewStories = addStoriesDataToCloudMapper.map(
                    addStoriesDomainToDataMapper.map(newStories)
                )
            ),
            onSuccess = { data ->
                val stories = createStoriesData(newStories, data)
                cacheDataSource.saveNewStoriesToCache(stories)
            }
        )

    private fun createStoriesData(
        newStories: AddStoriesDomain,
        postRequestAnswer: PostRequestAnswerCloud
    ) = StoriesData(
        title = newStories.title,
        description = newStories.description,
        userId = newStories.userId,
        schoolId = newStories.schoolId,
        imageFileUrl = newStories.imageFileUrl,
        videoFileUrl = newStories.videoFileUrl,
        previewImageUrl = newStories.previewImageUrl,
        isVideoFile = newStories.isVideoFile,
        publishedDate = postRequestAnswer.createdAt,
        storiesId = postRequestAnswer.objectId,
    )

    override suspend fun clearTable() {
        cacheDataSource.clearTable()
    }
}
