package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class SessionTokenCloud(
    @SerializedName("userSessionToken") val sessionToken: String,
)