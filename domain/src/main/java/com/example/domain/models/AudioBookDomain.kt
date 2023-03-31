package com.example.domain.models

import java.util.*

class AudioBookDomain(
    val id: String,
    val title: String,
    val description: String,
    val author: String,
    val schoolId: String,
    val createdAt: Date,
    val isExclusive: Boolean,
    val currentStartPosition: Int,
    val genres: List<String>,
    val audioBookFile: AudioBookFileDomain,
    val audioBookPoster: AudioBookPosterDomain,
    val isPlaying: Boolean,
)

data class AudioBookFileDomain(
    val name: String,
    val url: String
)

data class AudioBookPosterDomain(
    val name: String,
    val url: String
)