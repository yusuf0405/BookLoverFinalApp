package com.example.domain.domain.repository

import com.example.domain.models.Resource
import com.example.domain.domain.models.StudentBookDomain
import kotlinx.coroutines.flow.Flow


interface StudentBooksRepository {

    suspend fun fetchMyBooks(id: String): Flow<Resource<List<StudentBookDomain>>>

    suspend fun deleteMyBook(id: String): Flow<Resource<Unit>>
}