package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class UserUpdateCloud(
    @SerializedName("image") var image: UserImageCloud,
    @SerializedName("email") var email: String,
    @SerializedName("username") var username: String,
    @SerializedName("gender") var gender: String,
    @SerializedName("lastname") var lastname: String,
    @SerializedName("name") var name: String,
    @SerializedName("number") var number: String,
)