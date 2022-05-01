package com.example.domain.models.student

data class StudentUpdateRes(
    var image: UserDomainImage,
    var email: String,
    var gender: String,
    var lastname: String,
    var name: String,
    var number: String,
)
