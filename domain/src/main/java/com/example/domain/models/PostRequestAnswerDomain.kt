package com.example.domain.models

import java.util.*

data class PostRequestAnswerDomain(
    val id: String,
    val createdAt: Date,
    val image: UserDomainImage,
    val sessionToken: String,
) {

    companion object {
        fun unknown() = PostRequestAnswerDomain(
            id = String(),
            createdAt = Date(),
            image = UserDomainImage.unknown(),
            sessionToken = String()
        )
    }
}