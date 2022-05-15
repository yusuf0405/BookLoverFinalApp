package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class UpdateStudentClassCloud(
    @SerializedName("classId") val classId: String,
    @SerializedName("classsName") val classTitle: String,
)
