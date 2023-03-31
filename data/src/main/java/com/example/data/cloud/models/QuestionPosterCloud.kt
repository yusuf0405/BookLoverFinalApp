package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class QuestionPosterCloud(
    @SerializedName("poster") var poster: BookPosterCloud?,
)

data class QuestionPosterCloudResponse(
    @SerializedName("results")
    var posters: List<QuestionPosterCloud>,
)