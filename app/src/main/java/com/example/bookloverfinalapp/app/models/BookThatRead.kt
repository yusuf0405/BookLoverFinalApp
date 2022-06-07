package com.example.bookloverfinalapp.app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

@Parcelize
data class BookThatRead(
    var author: String,
    var createdAt: Date,
    var bookId: String,
    var objectId: String,
    var page: Int,
    var publicYear: String,
    var title: String,
    var chapterCount: Int,
    var chaptersRead: Int,
    var poster: BookThatReadPoster,
    var updatedAt: Date,
    var book: String,
    var progress: Int,
    var isReadingPages: List<Boolean>,
) : Parcelable {

    fun getCreatedAt(): String {
        val prettyTime = PrettyTime(Locale("ru"))
        return prettyTime.format(createdAt)
    }
}

@Parcelize
data class BookThatReadPoster(
    var name: String,
    var url: String,
) : Parcelable
