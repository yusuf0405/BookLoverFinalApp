package com.example.domain.models

data class UserUpdateDomain(
    var image: UserDomainImage,
    var email: String,
    var gender: String,
    var lastname: String,
    var name: String,
    var number: String,
)
