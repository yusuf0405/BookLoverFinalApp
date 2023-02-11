package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("results")
    var genres: List<GenreCloud>,
)