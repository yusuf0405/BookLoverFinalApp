package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class TaskCloud(
    @SerializedName("objectId") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("classId") val classId: String,
//    @SerializedName("startDate") val startDate: Date,
//    @SerializedName("endDate") val endDate: Date,
    @SerializedName("taskGenres") val taskGenres: List<String>,
)
