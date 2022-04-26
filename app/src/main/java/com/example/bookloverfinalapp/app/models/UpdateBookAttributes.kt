package com.example.bookloverfinalapp.app.models

data class Progress(
    val progress: Int,
)

data class Chapters(
    val chapters: Int,
    var isReadingPages: List<Boolean>,
)