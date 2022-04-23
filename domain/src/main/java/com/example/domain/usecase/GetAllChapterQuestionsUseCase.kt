package com.example.domain.usecase

import com.example.domain.repository.BookRepository

class GetAllChapterQuestionsUseCase(private val repository: BookRepository) {
    fun execute(id: String, chapter: Int) =
        repository.getAllChapterQuestions(id = id, chapter = chapter.toString())
}