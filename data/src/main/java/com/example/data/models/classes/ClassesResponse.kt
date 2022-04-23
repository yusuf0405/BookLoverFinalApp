package com.example.data.models.classes

import com.example.data.models.classes.ClassDto
import com.google.gson.annotations.SerializedName

data class ClassesResponse(
    @SerializedName("results") var classes: List<ClassDto>,
)