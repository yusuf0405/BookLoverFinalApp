package com.example.data.repository.audio_books

import com.example.data.cache.source.audio_books.AudioBooksCacheDataSource
import com.example.data.cloud.source.audio_books.AudioBookCloudDataSource
import com.example.data.models.AudioBookData
import com.example.data.repository.BaseRepository
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.AudioBookDomain
import com.example.domain.repository.AudioBooksRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AudioBooksRepositoryImpl @Inject constructor(
    private val cacheDataSource: AudioBooksCacheDataSource,
    private val cloudDataSource: AudioBookCloudDataSource,
    private val dispatchersProvider: DispatchersProvider,
    private val audioBookDataToDomainMapper: Mapper<AudioBookData, AudioBookDomain>
) : AudioBooksRepository, BaseRepository {

    override fun fetchAllAudioBooks(schoolId: String): Flow<List<AudioBookDomain>> = flow {
        emit(cacheDataSource.fetchAllAudioBooksFromCacheSingle())
    }.flatMapLatest { handleFetchBooksInCache(it, schoolId) }
        .map { audioBooks -> audioBooks.map(audioBookDataToDomainMapper::map) }
        .flowOn(dispatchersProvider.default())

    override fun fetchAllAudioBooksFromCache(): Flow<List<AudioBookDomain>> =
        cacheDataSource.fetchAllAudioBooksFromCacheObservable()
            .map { audioBooks -> audioBooks.map(audioBookDataToDomainMapper::map) }
            .flowOn(dispatchersProvider.default())

    private fun handleFetchBooksInCache(
        cachedBooks: List<AudioBookData>,
        schoolId: String
    ) = if (cachedBooks.isEmpty()) cloudDataSource.fetchAllAudioBooksFromCloud(schoolId = schoolId)
        .onEach { books -> books.forEach { cacheDataSource.saveNewAudioBooksToCache(it) } }
    else cacheDataSource.fetchAllAudioBooksFromCacheObservable()

    override fun fetchAudioBookFromCacheObservable(audioBookId: String): Flow<AudioBookDomain> =
        cacheDataSource
            .fetchAudioBookFromIdObservable(audioBookId = audioBookId)
            .map(audioBookDataToDomainMapper::map)

    override suspend fun updateAudioBookCurrentStartPosition(
        audioBookId: String,
        currentPosition: Int
    ) {
        cacheDataSource.updateAudioBookCurrentStartPosition(
            audioBookId = audioBookId,
            currentPosition = currentPosition
        )
    }

    override suspend fun updateAudioBookIsPlayingState(audioBookId: String, isPlaying: Boolean) {
        cacheDataSource.updateAudioBookIsPlayingState(
            audioBookId = audioBookId,
            isPlaying = isPlaying
        )
    }

    override suspend fun clearTable() {
        cacheDataSource.clearTable()
    }
}
