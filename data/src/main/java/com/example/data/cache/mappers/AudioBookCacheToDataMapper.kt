package com.example.data.cache.mappers

import com.example.data.cache.models.AudioBookCache
import com.example.data.models.AudioBookData
import com.example.data.models.AudioBookFileData
import com.example.data.models.AudioBookPosterData
import com.example.domain.Mapper
import javax.inject.Inject

class AudioBookCacheToDataMapper @Inject constructor() : Mapper<AudioBookCache, AudioBookData> {

    override fun map(from: AudioBookCache): AudioBookData = from.run {
        AudioBookData(
            title = title,
            author = author,
            createdAt = createdAt,
            id = id,
            schoolId = schoolId,
            currentStartPosition = 0,
            genres = genres,
            audioBookFile = AudioBookFileData(name = audioBookFile.name, url = audioBookFile.url),
            audioBookPoster = AudioBookPosterData(
                name = audioBookPoster.name,
                url = audioBookPoster.url
            ),
            isExclusive = isExclusive,
            description = description,
            isPlaying = isPlaying
        )
    }

}