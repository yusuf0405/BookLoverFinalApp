package com.example.data.models.student

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("results") var users: List<UserDto>,
)