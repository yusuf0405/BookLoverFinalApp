package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserResponse(
    @SerializedName("results") var users: List<UserCloud>,
)

data class UserCloud(
    @SerializedName("objectId") var objectId: String,
    @SerializedName("classId") var classId: String,
    @SerializedName("schoolId") var schoolId: String,
    @SerializedName("createdAt") var createAt: Date,
    @SerializedName("schoolName") var schoolName: String,
    @SerializedName("image") var image: UserImageCloud,
    @SerializedName("classsName") var className: String,
    @SerializedName("name") var name: String,
    @SerializedName("gender") var gender: String,
    @SerializedName("lastname") var lastname: String,
    @SerializedName("username") var email: String,
    @SerializedName("number") var number: String,
    @SerializedName("userType") var userType: String,
    @SerializedName("sessionToken") var sessionToken: String?,
    @SerializedName("userSessionToken") var userSessionToken: String,
) {
    companion object {

        fun unknown() = UserCloud(
            objectId = UUID.randomUUID().toString(),
            createAt = Date(),
            classId = String(),
            schoolId = String(),
            image = UserImageCloud(String(), String(), String()),
            email = String(),
            schoolName = String(),
            className = String(),
            gender = String(),
            lastname = String(),
            name = String(),
            number = String(),
            userType = String(),
            sessionToken = String(),
            userSessionToken = String()
        )
    }
}

data class UserImageCloud(
    @SerializedName("name") var name: String,
    @SerializedName("__type") var type: String,
    @SerializedName("url") var url: String,
) {
    companion object {

        fun unknown() = UserImageCloud(
            name = String(),
            type = String(),
            url = String()
        )
    }
}