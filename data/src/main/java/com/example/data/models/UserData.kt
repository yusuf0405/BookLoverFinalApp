package com.example.data.models


import java.util.*

data class UserData(
    var objectId: String,
    var classId: String,
    var schoolId: String,
    var createAt: Date,
    var schoolName: String,
    var image: UserImageData,
    var className: String,
    var email: String,
    var gender: String,
    var lastname: String,
    var name: String,
    var number: String,
    var userType: String,
    var sessionToken: String,
)

data class UserImageData(
    var name: String,
    var type: String,
    var url: String,
)