package com.example.domain.use_cases

import com.example.domain.repository.UserRepository

class StudentsOnRefreshUseCase(private var repository: UserRepository) {
    fun execute(classId: String) = repository.onRefresh(classId = classId)
}