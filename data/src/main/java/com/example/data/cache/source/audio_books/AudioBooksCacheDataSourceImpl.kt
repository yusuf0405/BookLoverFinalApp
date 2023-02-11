package com.example.data.cache.source.audio_books

import com.example.data.cache.db.AudioBooksDao
import com.example.data.cache.models.AudioBookCache
import com.example.data.models.AudioBookData
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AudioBooksCacheDataSourceImpl @Inject constructor(
    private val dao: AudioBooksDao,
    private val dispatchersProvider: DispatchersProvider,
    private val audioBookCacheToDataMapper: Mapper<AudioBookCache, AudioBookData>,
    private val audioBookDataToCacheMapper: Mapper<AudioBookData, AudioBookCache>
) : AudioBooksCacheDataSource {

    override fun fetchAllAudioBooksFromCacheObservable(): Flow<List<AudioBookData>> =
        dao.fetchAllAudioBooksObservable()
            .flowOn(dispatchersProvider.io())
            .map { books -> books.map(audioBookCacheToDataMapper::map) }
            .flowOn(dispatchersProvider.default())

    override suspend fun fetchAllAudioBooksFromCacheSingle(): List<AudioBookData> =
        dao.fetchAllAudioBooksSingle().map(audioBookCacheToDataMapper::map)

    override suspend fun saveNewAudioBooksToCache(audioBook: AudioBookData) {
        dao.addNewBook(audioBook = audioBookDataToCacheMapper.map(audioBook))
    }

    override suspend fun fetchAudioBookFromId(audioBookId: String): AudioBookData {
        val audioBook = dao.fetchAudioBookFromId(audioBookId)
        return audioBookCacheToDataMapper.map(audioBook)
    }

    override suspend fun updateAudioBookCurrentStartPosition(
        audioBookId: String,
        currentPosition: Int
    ) {
        dao.updateAudioBookCurrentStartPosition(
            audioBookId = audioBookId,
            currentPosition = currentPosition
        )
    }

    override suspend fun clearTable() {
        dao.clearTable()
    }
}