package com.example.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "users_database")
data class StudentDb(
    @PrimaryKey var objectId: String,
    @ColumnInfo(name = "classId") var classId: String,
    @ColumnInfo(name = "createAt") var createAt: Date,
    @ColumnInfo(name = "schoolName") var schoolName: String,
    @ColumnInfo(name = "className") var className: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "gender") var gender: String,
    @ColumnInfo(name = "lastname") var lastname: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "number") var number: String,
    @ColumnInfo(name = "sessionToken") var sessionToken: String,
    @ColumnInfo(name = "userType") var userType: String,
    @ColumnInfo(name = "chaptersRead") var chaptersRead: Int,
    @ColumnInfo(name = "booksRead") var booksRead: Int,
    @ColumnInfo(name = "progress") var progress: Int,
    @ColumnInfo(name = "booksId") val booksId: List<String>,
    @ColumnInfo(name = "image") var image: StudentImageDb,
)

data class StudentImageDb(
    var name: String,
    var type: String,
    var url: String,
)
