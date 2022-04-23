package com.example.data.data.cache.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "student_book_database")
data class StudentBookDb(
    @PrimaryKey var objectId: String,
    var author: String,
    var createdAt: Date,
    var bookId: String,
    var page: Int,
    var publicYear: String,
    var title: String,
    var chapterCount: Int,
    var chaptersRead: Int,
    var poster: StudentBookPosterDb,
    var updatedAt: String,
    var book: StudentBookPdfDb,
    var progress: Int,
    var isReadingPages: List<Boolean>,
)

data class StudentBookPdfDb(
    var name: String,
    var url: String,
)

data class StudentBookPosterDb(
    var name: String,
    var url: String,
)
