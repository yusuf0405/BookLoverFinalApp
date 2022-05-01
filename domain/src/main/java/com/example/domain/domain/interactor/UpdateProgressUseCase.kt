package com.example.domain.domain.interactor

import com.example.domain.domain.models.ProgressDomain
import com.example.domain.domain.repository.BookThatReadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class UpdateProgressUseCase(private val repository: BookThatReadRepository) {
    fun execute(id: String, progress: ProgressDomain) =
        repository.updateProgress(id = id, progress = progress).flowOn(Dispatchers.IO)
}