package com.example.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "users")
data class UserCache(
    @PrimaryKey var objectId: String,
    @ColumnInfo(name = "class_id") var classId: String,
    @ColumnInfo(name = "create_at") var createAt: Date,
    @ColumnInfo(name = "school_name") var schoolName: String,
    @ColumnInfo(name = "class_name") var className: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "gender") var gender: String,
    @ColumnInfo(name = "lastname") var lastname: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "number") var number: String,
    @ColumnInfo(name = "session_token") var sessionToken: String,
    @ColumnInfo(name = "user_type") var userType: String,
    @ColumnInfo(name = "chapters_read") var chaptersRead: Int,
    @ColumnInfo(name = "books_read") var booksRead: Int,
    @ColumnInfo(name = "progress") var progress: Int,
    @ColumnInfo(name = "books_id") val booksId: List<String>,
    @ColumnInfo(name = "image") var image: UserImageCache,
)

data class UserImageCache(
    var name: String,
    var type: String,
    var url: String,
)
