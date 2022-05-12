package com.example.domain.models

data class BookQuestionDomain(
    val id: String,
    val question: String,
    val a: String,
    val b: String,
    val d: String,
    val c: String,
    val rightAnswer: String,
    val bookId: String,
    val chapter: String,
)
