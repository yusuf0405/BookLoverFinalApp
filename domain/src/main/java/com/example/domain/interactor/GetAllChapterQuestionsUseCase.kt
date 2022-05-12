package com.example.domain.interactor

import com.example.domain.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GetAllChapterQuestionsUseCase(private val repository: BooksRepository) {
    fun execute(id: String, chapter: Int) =
        repository.getAllChapterQuestions(id = id, chapter = chapter.toString()).flowOn(Dispatchers.IO)
}