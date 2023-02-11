package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class TaskResponse(
    @SerializedName("results")
    var tasks: List<TaskCloud>,
)