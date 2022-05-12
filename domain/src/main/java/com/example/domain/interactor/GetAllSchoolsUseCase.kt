package com.example.domain.interactor

import com.example.domain.Resource
import com.example.domain.models.SchoolDomain
import com.example.domain.repository.SchoolRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetAllSchoolsUseCase(private val repository: SchoolRepository) {
    fun execute(): Flow<Resource<List<SchoolDomain>>> =
        repository.getAllSchools().flowOn(Dispatchers.IO)
}