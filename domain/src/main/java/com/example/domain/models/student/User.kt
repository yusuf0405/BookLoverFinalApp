package com.example.domain.models.student

import java.util.*


data class User(
    val id: String,
    var createAt: Date,
    var classId: String,
    var image: UserImage? = null,
    var email: String,
    var schoolName: String,
    var className: String,
    var gender: String,
    var lastname: String,
    var name: String,
    var number: String,
    var password: String? = null,
    var userType: String,
    var sessionToken: String,
)