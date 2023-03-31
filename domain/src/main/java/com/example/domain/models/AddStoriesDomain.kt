package com.example.domain.models

data class AddStoriesDomain(
    val userId: String,
    val schoolId: String,
    val title: String,
    val description: String,
    val imageFileUrl: String,
    var previewImageUrl: String,
    var videoFileUrl: String,
    val isVideoFile: Boolean,
)