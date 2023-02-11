package com.example.data.mappers

import com.example.data.cache.models.AudioBookCache
import com.example.data.cache.models.AudioBookFileCache
import com.example.data.cache.models.AudioBookPosterCache
import com.example.data.models.AudioBookData
import com.example.data.models.AudioBookFileData
import com.example.data.models.AudioBookPosterData
import com.example.domain.Mapper
import javax.inject.Inject

class AudioBookDataToCacheMapper @Inject constructor() : Mapper<AudioBookData, AudioBookCache> {

    override fun map(from: AudioBookData): AudioBookCache = from.run {
        AudioBookCache(
            title = title,
            author = author,
            createdAt = createdAt,
            id = id,
            schoolId = schoolId,
            currentStartPosition = 0,
            genres = genres,
            audioBookFile = AudioBookFileCache(name = audioBookFile.name, url = audioBookFile.url),
            audioBookPoster = AudioBookPosterCache(
                name = audioBookPoster.name,
                url = audioBookPoster.url
            )
        )
    }

}