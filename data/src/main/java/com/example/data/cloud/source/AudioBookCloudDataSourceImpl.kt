package com.example.data.cloud.source

import com.example.data.cloud.models.AudioBookCloud
import com.example.data.cloud.models.AudioBookResponse
import com.example.data.cloud.service.AudioBookService
import com.example.data.models.AudioBookData
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AudioBookCloudDataSourceImpl @Inject constructor(
    private var service: AudioBookService,
    private var dispatchersProvider: DispatchersProvider,
    private var audioBookCloudToDataMapper: Mapper<AudioBookCloud, AudioBookData>
) : AudioBookCloudDataSource {

    override fun fetchAllAudioBooksFromCloud(schoolId: String): Flow<List<AudioBookData>> = flow {
        emit(service.fetchAllAudioBooks(id = "{\"schoolId\":\"${schoolId}\"}"))
    }.flowOn(dispatchersProvider.io())
        .map { it.body() ?: AudioBookResponse(emptyList()) }
        .map { it.audioBooks }
        .map { books -> books.map(audioBookCloudToDataMapper::map) }
        .flowOn(dispatchersProvider.default())
}