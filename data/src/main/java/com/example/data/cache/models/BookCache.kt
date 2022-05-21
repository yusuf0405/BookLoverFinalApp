package com.example.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "book_database")
data class BookDb(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "createdAt") var createdAt: Date,
    @ColumnInfo(name = "page") var page: Int,
    @ColumnInfo(name = "publicYear") var publicYear: String,
    @ColumnInfo(name = "book") var book: BookPdfDb,
    @ColumnInfo(name = "genres") var genres: List<String>,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "chapterCount") var chapterCount: Int,
    @ColumnInfo(name = "poster") var poster: BookPosterDb,
    @ColumnInfo(name = "updatedAt") var updatedAt: Date,
)

data class BookPdfDb(
    var name: String,
    var type: String,
    var url: String,
)

data class BookPosterDb(
    var name: String,
    var url: String,
)
