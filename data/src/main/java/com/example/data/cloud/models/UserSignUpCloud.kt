package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class UserSignUpCloud(
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("number") val number: String,
    @SerializedName("classsName") val className: String,
    @SerializedName("schoolName") val schoolName: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("classId") val classId: String,
    @SerializedName("schoolId") val schoolId: String,
    @SerializedName("userType") val userType: String,
    @SerializedName("userSessionToken") val userSessionToken: String,
)