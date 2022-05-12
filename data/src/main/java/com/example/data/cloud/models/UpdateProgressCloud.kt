package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

class UpdateProgressCloud(
    @SerializedName("progress") val progress: Int,
)