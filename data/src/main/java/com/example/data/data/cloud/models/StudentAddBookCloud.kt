package com.example.data.data.cloud.models

import com.example.domain.domain.models.BookThatReadDomain
import com.google.gson.annotations.SerializedName

data class StudentAddBookCloud(
    @SerializedName("books") var books: List<BookThatReadDomain>,
)