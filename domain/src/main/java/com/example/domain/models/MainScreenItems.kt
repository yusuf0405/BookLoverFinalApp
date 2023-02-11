package com.example.domain.models

data class MainScreenItems(
    val books: List<BookDomain>,
    val savedBooks: List<BookThatReadDomain>,
    val users: List<StudentDomain>,
    val audioBooks: List<AudioBookDomain>,
    val tasks: List<TaskDomain>,
    val genres: List<GenreDomain>,
    val isTeacher: Boolean,
)