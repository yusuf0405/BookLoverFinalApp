package com.example.domain.interactor

import com.example.domain.repository.BookThatReadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class UpdateProgressUseCase(private val repository: BookThatReadRepository) {
    fun execute(id: String, progress: Int) =
        repository.updateProgress(id = id, progress = progress).flowOn(Dispatchers.IO)
}