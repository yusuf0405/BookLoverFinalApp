package com.example.data.models.school

import com.google.gson.annotations.SerializedName

data class SchoolResponse(
    @SerializedName("results") var school: List<SchoolDto>,
)