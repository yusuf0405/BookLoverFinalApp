package com.example.data.models

import java.util.*


data class UserSaveModel(
    val id: String,
    var createAt: Date,
    var classId: String,
    var schoolId: String,
    var image: UserSaveModelImage,
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
) {
    companion object {
        fun unknown() = UserSaveModel(
            id = UUID.randomUUID().toString(),
            createAt = Date(),
            classId = String(),
            schoolId = String(),
            image = UserSaveModelImage(String(), String(), String()),
            email = String(),
            schoolName = String(),
            className = String(),
            gender = String(),
            lastname = String(),
            name = String(),
            number = String(),
            password = String(),
            userType = String(),
            sessionToken = String(),
        )
    }
}

data class UserSaveModelImage(
    var name: String,
    var type: String,
    var url: String,
) {
    companion object {
        fun unknown() = UserSaveModelImage(
            name = String(),
            type = String(),
            url = String(),
        )
    }
}