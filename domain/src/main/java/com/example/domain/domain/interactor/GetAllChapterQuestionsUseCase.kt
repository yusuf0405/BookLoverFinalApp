package com.example.domain.domain.interactor

import com.example.domain.domain.repository.BooksRepository

class GetAllChapterQuestionsUseCase(private val repository: BooksRepository) {
    fun execute(id: String, chapter: Int) =
        repository.getAllChapterQuestions(id = id, chapter = chapter.toString())
}