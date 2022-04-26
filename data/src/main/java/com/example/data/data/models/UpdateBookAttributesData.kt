package com.example.data.data.models

data class ProgressData(
    val progress: Int,
)

data class ChaptersData(
    val isReadingPages: List<Boolean>,
    val chaptersRead: Int,
)