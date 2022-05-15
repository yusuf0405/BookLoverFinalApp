package com.example.domain.interactor

import com.example.domain.repository.ClassRepository

class AddNewClassUseCase(private var repository: ClassRepository) {
    fun execute(title: String, schoolId: String) =
        repository.addClass(title = title, schoolId = schoolId)
}