package com.example.data.models

import java.util.Date

class AudioBookData(
    val id: String,
    val title: String,
    val author: String,
    val schoolId: String,
    val description: String,
    val createdAt: Date,
    val isExclusive: Boolean,
    val currentStartPosition: Int,
    val genres: List<String>,
    val audioBookFile: AudioBookFileData,
    val audioBookPoster: AudioBookPosterData,
    val isPlaying: Boolean,
) {
    companion object {
        val unknown = AudioBookData(
            id = String(),
            title = String(),
            author = String(),
            schoolId = String(),
            description = String(),
            createdAt = Date(),
            currentStartPosition = 0,
            genres = emptyList(),
            audioBookFile = AudioBookFileData.unknown,
            audioBookPoster = AudioBookPosterData.unknown,
            isExclusive = false,
            isPlaying = false
        )
    }
}

data class AudioBookFileData(
    val name: String,
    val url: String
) {
    companion object {
        val unknown = AudioBookFileData(
            name = String(),
            url = String(),
        )
    }
}

data class AudioBookPosterData(
    val name: String,
    val url: String
) {
    companion object {
        val unknown = AudioBookPosterData(
            name = String(),
            url = String(),
        )
    }
}