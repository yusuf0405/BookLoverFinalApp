package com.example.bookloverfinalapp.app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookQuestion(
    val id: String,
    val question: String,
    val a: String,
    val b: String,
    val d: String,
    val c: String,
    val rightAnswer: String,
    val bookId: String,
    val chapter: String,
) : Parcelable