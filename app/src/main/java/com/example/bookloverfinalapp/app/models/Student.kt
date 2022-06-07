package com.example.bookloverfinalapp.app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Student(
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
    var image: StudentImage,
) : Parcelable {
    fun fullName(): String = "$name $lastname"

    fun getCreatedAt(): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        return formatter.format(createAt).toString()

    }

}

@Parcelize
data class StudentImage(
    var name: String,
    var type: String,
    var url: String,
) : Parcelable
