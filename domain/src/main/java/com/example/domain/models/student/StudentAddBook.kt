package com.example.domain.models.student

import com.example.domain.domain.models.StudentBookDomain

data class StudentAddBook(
    var books: List<StudentBookDomain>,
)