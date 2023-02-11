package com.example.data.models

import java.util.Date

class AudioBookData(
    val id: String,
    val title: String,
    val author: String,
    val schoolId: String,
    val createdAt: Date,
    val currentStartPosition: Int,
    val genres: List<String>,
    val audioBookFile: AudioBookFileData,
    val audioBookPoster: AudioBookPosterData
)

data class AudioBookFileData(
    val name: String,
    val url: String
)

data class AudioBookPosterData(
    val name: String,
    val url: String
)