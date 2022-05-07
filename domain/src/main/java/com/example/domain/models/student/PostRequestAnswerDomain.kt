package com.example.domain.models.student

import com.example.domain.domain.models.UserDomainImage
import java.util.*

data class PostRequestAnswerDomain(
    val id: String,
    val createdAt: Date,
    val image: UserDomainImage,
    val sessionToken: String,
)