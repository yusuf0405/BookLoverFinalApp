package com.example.domain.models.student

import java.util.*

data class PostRequestAnswer(
    val id: String,
    val createdAt: Date,
    val image: UserImage,
    val sessionToken: String,
)