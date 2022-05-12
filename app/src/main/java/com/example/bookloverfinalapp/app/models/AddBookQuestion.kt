package com.example.bookloverfinalapp.app.models

data class AddBookQuestion(
    val question: String,
    val a: String,
    val b: String,
    val d: String,
    val c: String,
    val rightAnswer: String,
    val bookId: String,
    val chapter: String,
)