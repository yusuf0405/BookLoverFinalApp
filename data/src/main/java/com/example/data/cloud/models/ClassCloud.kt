package com.example.data.cloud.models


import com.google.gson.annotations.SerializedName

data class ClassResponse(
    @SerializedName("results") var classes: List<ClassCloud>,
)

data class ClassCloud(
    @SerializedName("objectId") var objectId: String,
    @SerializedName("title") var title: String,
    @SerializedName("schoolId") var schoolId: String,
)