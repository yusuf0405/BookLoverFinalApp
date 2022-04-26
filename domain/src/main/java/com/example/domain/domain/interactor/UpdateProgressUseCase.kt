package com.example.domain.domain.interactor

import com.example.domain.domain.models.ProgressDomain
import com.example.domain.domain.repository.StudentBooksRepository

class UpdateProgressUseCase(private val repository: StudentBooksRepository) {

    fun execute(id: String, progress: ProgressDomain) =
        repository.updateProgress(id = id, progress = progress)
}