package com.example.domain.domain.interactor

import com.example.domain.domain.models.ChaptersDomain
import com.example.domain.domain.repository.BookThatReadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class UpdateChaptersUseCase(private val repository: BookThatReadRepository) {

    fun execute(id: String, chaptersDomain: ChaptersDomain) =
        repository.updateChapters(id = id, chapters = chaptersDomain).flowOn(Dispatchers.IO)
}