package com.example.data.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "book_that_read_database")
data class BookThatReadDb(
    @PrimaryKey var objectId: String,
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "createdAt") var createdAt: Date,
    @ColumnInfo(name = "bookId") var bookId: String,
    @ColumnInfo(name = "page") var page: Int,
    @ColumnInfo(name = "publicYear") var publicYear: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "chapterCount") var chapterCount: Int,
    @ColumnInfo(name = "chaptersRead") var chaptersRead: Int,
    @ColumnInfo(name = "poster") var poster: BookThatReadPosterDb,
    @ColumnInfo(name = "updatedAt") var updatedAt: Date,
    @ColumnInfo(name = "book") var book: String,
    @ColumnInfo(name = "progress") var progress: Int,
    @ColumnInfo(name = "isReadingPages") var isReadingPages: List<Boolean>,
)

data class BookThatReadPosterDb(
    var name: String,
    var url: String,
)
