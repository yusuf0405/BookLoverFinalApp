package com.example.data.models.student

import com.example.data.data.models.UserImageData
import com.google.gson.annotations.SerializedName

data class UserUpdateRequest(
    @SerializedName("image") var image: UserImageData,
    @SerializedName("email") var email: String,
    @SerializedName("gender") var gender: String,
    @SerializedName("lastname") var lastname: String,
    @SerializedName("username") var name: String,
    @SerializedName("number") var number: String,
)