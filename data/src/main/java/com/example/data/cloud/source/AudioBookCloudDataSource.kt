package com.example.data.cloud.source

import com.example.data.models.AudioBookData
import kotlinx.coroutines.flow.Flow

interface AudioBookCloudDataSource {

    fun fetchAllAudioBooksFromCloud(schoolId: String): Flow<List<AudioBookData>>
}