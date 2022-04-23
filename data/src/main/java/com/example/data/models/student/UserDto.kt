package com.example.data.models.student


import com.google.gson.annotations.SerializedName
import java.util.*

data class UserDto(
    @SerializedName("objectId") var objectId: String,
    @SerializedName("classId") var classId: String,
    @SerializedName("createdAt") var createAt: Date,
    @SerializedName("schoolName") var schoolName: String,
    @SerializedName("image") var image: UserImageDto,
    @SerializedName("className") var className: String,
    @SerializedName("email") var email: String,
    @SerializedName("gender") var gender: String,
    @SerializedName("lastname") var lastname: String,
    @SerializedName("username") var name: String,
    @SerializedName("number") var number: String,
    @SerializedName("userType") var userType: String,
    @SerializedName("sessionToken") var sessionToken: String,
)