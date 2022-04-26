package com.example.domain.domain.interactor

import com.example.domain.domain.models.StudentBookDomain
import com.example.domain.domain.repository.StudentBooksRepository
import com.example.domain.models.Resource
import kotlinx.coroutines.flow.Flow

class GetMyBookUseCase(private val repository: StudentBooksRepository) {

    fun execute(id: String): Flow<Resource<StudentBookDomain>> = repository.getMyBook(id = id)

}