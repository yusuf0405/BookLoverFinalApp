package com.example.domain.domain.interactor

import com.example.domain.domain.models.StudentBookDomain
import com.example.domain.domain.repository.StudentBooksRepository
import com.example.domain.models.Resource
import com.example.domain.models.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException

class GetStudentBookUseCase(private val repository: StudentBooksRepository) {
    suspend fun execute(id: String): Flow<Resource<List<StudentBookDomain>>> = repository.fetchMyBooks(id = id)
}