package com.example.bookloverfinalapp.app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Book(
    var author: String,
    var createdAt: Date,
    var objectId: String,
    var page: Int,
    var genres: List<String>,
    var publicYear: String,
    var title: String,
    var chapterCount: Int,
    var poster: BookPoster,
    var updatedAt: Date,
    var book: BookPdf,
) : Parcelable

@Parcelize
data class BookPdf(
    var name: String,
    var url: String,
) : Parcelable

@Parcelize
data class BookPoster(
    var name: String,
    var url: String,
) : Parcelable

