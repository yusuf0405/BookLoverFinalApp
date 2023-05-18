package com.joseph.profile.domain.models

data class UserUpdateModel(
    val id: String,
    val sessionToken: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)