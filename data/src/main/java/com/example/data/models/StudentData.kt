package com.example.data.models

import java.util.*

data class StudentData(
    var objectId: String,
    var classId: String,
    var createAt: Date,
    var schoolName: String,
    var className: String,
    var email: String,
    var gender: String,
    var lastname: String,
    var name: String,
    val sessionToken: String,
    var number: String,
    var userType: String,
    var chaptersRead: Int,
    var booksRead: Int,
    var progress: Int,
    val booksId: List<String>,
    var image: StudentImageData,
)

data class StudentImageData(
    var name: String,
    var type: String,
    var url: String,
)