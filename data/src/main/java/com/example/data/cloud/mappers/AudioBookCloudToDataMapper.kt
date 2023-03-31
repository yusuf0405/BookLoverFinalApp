package com.example.data.cloud.mappers

import com.example.data.cloud.models.AudioBookCloud
import com.example.data.models.AudioBookData
import com.example.data.models.AudioBookFileData
import com.example.data.models.AudioBookPosterData
import com.example.domain.Mapper
import javax.inject.Inject

class AudioBookCloudToDataMapper @Inject constructor() : Mapper<AudioBookCloud, AudioBookData> {

    override fun map(from: AudioBookCloud): AudioBookData = from.run {
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
            isPlaying = false
        )
    }
}