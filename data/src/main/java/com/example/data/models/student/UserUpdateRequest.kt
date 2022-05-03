package com.example.data.models.student

import com.example.data.data.cloud.models.UserImageCloud
import com.example.data.data.models.UserImageData
import com.google.gson.annotations.SerializedName

data class UserUpdateRequest(
    @SerializedName("image") var image: UserImageCloud,
    @SerializedName("username") var email: String,
    @SerializedName("gender") var gender: String,
    @SerializedName("lastname") var lastname: String,
    @SerializedName("name") var name: String,
    @SerializedName("number") var number: String,
)