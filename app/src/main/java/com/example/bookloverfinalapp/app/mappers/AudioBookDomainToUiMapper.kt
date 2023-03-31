package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.models.AudioBookFile
import com.example.bookloverfinalapp.app.models.AudioBookPoster
import com.example.domain.Mapper
import com.example.domain.models.AudioBookDomain
import javax.inject.Inject

class AudioBookDomainToUiMapper @Inject constructor() : Mapper<AudioBookDomain, AudioBook> {

    override fun map(from: AudioBookDomain): AudioBook = from.run {
        AudioBook(
            title = title,
            author = author,
            createdAt = createdAt,
            id = id,
            schoolId = schoolId,
            currentStartPosition = 0,
            genres = genres,
            audioBookFile = AudioBookFile(name = audioBookFile.name, url = audioBookFile.url),
            audioBookPoster = AudioBookPoster(
                name = audioBookPoster.name,
                url = audioBookPoster.url
            ),
            description = description,
            isExclusive = isExclusive,
            isPlaying = isPlaying
        )
    }

}