package com.example.domain.domain.models

data class ProgressDomain(
    val progress: Int,
)

data class ChaptersDomain(
    val isReadingPages: List<Boolean>,
    val chaptersRead: Int,
)