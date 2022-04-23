package com.example.data.models.school

import com.google.gson.annotations.SerializedName

data class SchoolDto(
    @SerializedName("objectId") var objectId: String,
    @SerializedName("title") var title: String,
    @SerializedName("classesIds") var classes: List<String>,
)