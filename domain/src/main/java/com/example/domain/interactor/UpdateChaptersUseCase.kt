package com.example.domain.interactor

import com.example.domain.repository.BookThatReadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class UpdateChaptersUseCase(private val repository: BookThatReadRepository) {

    fun execute(
        id: String, chapters: Int, isReadingPages: List<Boolean>, ) =
        repository.updateChapters(id = id, chapters = chapters, isReadingPages = isReadingPages).flowOn(Dispatchers.IO)
}