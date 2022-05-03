package com.example.domain.models.student

import com.example.domain.domain.models.UserDomainImage

data class StudentUpdateRes(
    var image: UserDomainImage,
    var email: String,
    var gender: String,
    var lastname: String,
    var name: String,
    var number: String,
)
