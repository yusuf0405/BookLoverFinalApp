package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class SchoolResponse(
    @SerializedName("results") var school: List<SchoolCloud>,
)

data class SchoolCloud(
    @SerializedName("objectId") var objectId: String,
    @SerializedName("title") var title: String,
)