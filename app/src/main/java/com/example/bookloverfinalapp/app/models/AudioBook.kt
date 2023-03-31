package com.example.bookloverfinalapp.app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
class AudioBook(
    val id: String,
    val title: String,
    val description: String,
    val author: String,
    val schoolId: String,
    val createdAt: Date,
    val isExclusive: Boolean,
    val currentStartPosition: Int,
    val genres: List<String>,
    val audioBookFile: AudioBookFile,
    val audioBookPoster: AudioBookPoster,
    val isPlaying: Boolean,
) : Parcelable {

    companion object {
        fun unknown() = AudioBook(
            id = String(),
            title = String(),
            author = String(),
            schoolId = String(),
            createdAt = Date(),
            currentStartPosition = 0,
            genres = emptyList(),
            audioBookFile = AudioBookFile(String(), String()),
            audioBookPoster = AudioBookPoster(String(), String()),
            description = String(),
            isExclusive = false,
            isPlaying = false
        )
    }
}

@Parcelize
data class AudioBookFile(
    val name: String,
    val url: String
) : Parcelable

@Parcelize
data class AudioBookPoster(
    val name: String,
    val url: String
) : Parcelable