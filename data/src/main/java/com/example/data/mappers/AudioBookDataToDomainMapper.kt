package com.example.data.mappers

import com.example.data.models.AudioBookData
import com.example.domain.Mapper
import com.example.domain.models.AudioBookDomain
import com.example.domain.models.AudioBookFileDomain
import com.example.domain.models.AudioBookPosterDomain
import javax.inject.Inject

class AudioBookDataToDomainMapper @Inject constructor() : Mapper<AudioBookData, AudioBookDomain> {

    override fun map(from: AudioBookData): AudioBookDomain = from.run {
        AudioBookDomain(
            title = title,
            author = author,
            createdAt = createdAt,
            id = id,
            schoolId = schoolId,
            currentStartPosition = 0,
            genres = genres,
            audioBookFile = AudioBookFileDomain(name = audioBookFile.name, url = audioBookFile.url),
            audioBookPoster = AudioBookPosterDomain(
                name = audioBookPoster.name,
                url = audioBookPoster.url
            ),
            isExclusive = isExclusive,
            description = description,
            isPlaying = isPlaying
        )
    }

}