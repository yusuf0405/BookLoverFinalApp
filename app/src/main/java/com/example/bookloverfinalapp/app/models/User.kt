package com.example.bookloverfinalapp.app.models

import com.example.bookloverfinalapp.app.utils.UserType
import java.util.*

data class User(
    val id: String,
    var createAt: Date,
    var classId: String,
    var schoolId: String,
    var image: UserImage? = null,
    var email: String,
    var schoolName: String,
    var className: String,
    var gender: String,
    var lastname: String,
    var name: String,
    var number: String,
    var password: String? = null,
    var userType: UserType,
    var sessionToken: String,
)

data class UserImage(
    var name: String,
    var type: String,
    var url: String,
)