package com.example.data.data.cloud.models

import com.google.gson.annotations.SerializedName

data class PasswordResetCloud(
    @SerializedName("email") val email: String,
)