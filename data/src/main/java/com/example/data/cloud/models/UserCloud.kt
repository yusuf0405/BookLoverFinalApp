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
    @SerializedName("sessionToken") var sessionToken: String,
    @SerializedName("userSessionToken") var userSessionToken: String,
)

data class UserImageCloud(
    @SerializedName("name") var name: String,
    @SerializedName("__type") var type: String,
    @SerializedName("url") var url: String,
)