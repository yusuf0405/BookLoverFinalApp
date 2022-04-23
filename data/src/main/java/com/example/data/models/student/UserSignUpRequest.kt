package com.example.data.models.student

import com.google.gson.annotations.SerializedName

data class UserSignUpRequest(
    @SerializedName("username") val name: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("number") val number: String,
    @SerializedName("className") val className: String,
    @SerializedName("schoolName") val schoolName: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("classId") val classId: String,
    @SerializedName("userType") val userType: String,
)