package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    @SerializedName("results")
    var stories: List<StoriesCloud>,
)