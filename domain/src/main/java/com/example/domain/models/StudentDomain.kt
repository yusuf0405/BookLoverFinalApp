package com.example.domain.models

import java.util.*

data class StudentDomain(
    var objectId: String,
    var classId: String,
    var createAt: Date,
    var schoolName: String,
    var className: String,
    var sessionToken: String,
    var email: String,
    var gender: String,
    var lastname: String,
    var name: String,
    var number: String,
    var userType: String,
    var chaptersRead: Int,
    var booksRead: Int,
    var progress: Int,
    val booksId: List<String>,
    var image: StudentImageDomain,
)

data class StudentImageDomain(
    var name: String,
    var type: String,
    var url: String,
)
