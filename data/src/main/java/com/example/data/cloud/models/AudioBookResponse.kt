package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class AudioBookResponse(
    @SerializedName("results")
    var audioBooks: List<AudioBookCloud>,
)