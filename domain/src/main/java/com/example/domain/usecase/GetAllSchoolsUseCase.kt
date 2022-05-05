package com.example.domain.usecase

import com.example.domain.models.Resource
import com.example.domain.models.school.School
import com.example.domain.domain.repository.SchoolRepository
import kotlinx.coroutines.flow.Flow

class GetAllSchoolsUseCase(private val repository: SchoolRepository) {
    fun execute(): Flow<Resource<List<School>>> = repository.getAllSchools()
}