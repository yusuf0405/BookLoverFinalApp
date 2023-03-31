package com.example.domain.models

import java.util.*

data class StoriesDomain(
    val storiesId: String,
    val userId: String,
    val schoolId: String,
    val title: String,
    val description: String,
    val imageFileUrl: String,
    var previewImageUrl: String,
    var videoFileUrl: String,
    val isVideoFile: Boolean,
    val publishedDate: Date,
)