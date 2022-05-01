package com.example.domain.models.student

import com.example.domain.domain.models.BookThatReadDomain

data class StudentAddBook(
    var books: List<BookThatReadDomain>,
)