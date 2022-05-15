package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class AddClassCloud(
    @SerializedName("title") var title: String,
    @SerializedName("schoolId") var schoolId: String,
)