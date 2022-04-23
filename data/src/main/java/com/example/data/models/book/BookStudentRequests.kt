package com.example.data.models.book

import com.google.gson.annotations.SerializedName

data class BookStudentRequests(
    @SerializedName("results") val results: List<BookStudentRequest>,
)