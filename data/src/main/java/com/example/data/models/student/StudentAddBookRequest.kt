package com.example.data.models.student

import com.example.domain.domain.models.StudentBookDomain
import com.google.gson.annotations.SerializedName

data class StudentAddBookRequest(
    @SerializedName("books") var books: List<StudentBookDomain>,
)