package com.example.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "books")
data class BookCache(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "created_at") var createdAt: Date,
    @ColumnInfo(name = "page") var page: Int,
    @ColumnInfo(name = "public_year") var publicYear: String,
    @ColumnInfo(name = "book") var book: BookPdfCache,
    @ColumnInfo(name = "genres") var genres: List<String>,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "chapter_count") var chapterCount: Int,
    @ColumnInfo(name = "poster") var poster: BookPosterCache,
    @ColumnInfo(name = "updated_at") var updatedAt: Date,
)

data class BookPdfCache(
    var name: String,
    var type: String,
    var url: String,
)

data class BookPosterCache(
    var name: String,
    var url: String,
)
