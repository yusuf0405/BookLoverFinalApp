package com.example.data.models.classes


import com.google.gson.annotations.SerializedName

data class ClassDto(
    @SerializedName("objectId") var objectId: String,
    @SerializedName("teachersIds") var teachers: List<String>? = null,
    @SerializedName("title") var title: String,
    @SerializedName("studentsIds") var students: List<String>? = null,
)