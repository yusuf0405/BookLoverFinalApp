package com.example.domain.models

data class MainScreenItems(
    val books: List<BookDomain>,
    val currentUser: UserDomain,
    val savedBooks: List<BookThatReadDomain>,
    val users: List<StudentDomain>,
    val audioBooks: List<AudioBookDomain>,
    val stories: List<StoriesDomain>,
    val tasks: List<TaskDomain>,
    val genres: List<GenreDomain>,
    val isTeacher: Boolean,
)