package com.example.domain.interactor

import com.example.domain.repository.ClassRepository

class GetAllClassesCloudUseCase(private var repository: ClassRepository) {
    fun execute(schoolId: String) = repository.fetchAllClassCloud(schoolId = schoolId)
}