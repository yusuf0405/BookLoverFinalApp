package com.example.data.models

data class AddStoriesData(
    val userId: String,
    val schoolId: String,
    val title: String,
    val description: String,
    val imageFileUrl: String,
    var previewImageUrl: String,
    var videoFileUrl: String,
    val isVideoFile: Boolean,
)