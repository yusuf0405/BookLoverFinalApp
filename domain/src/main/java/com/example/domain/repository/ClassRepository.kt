package com.example.domain.repository

import com.example.domain.Resource
import com.example.domain.models.ClassDomain
import kotlinx.coroutines.flow.Flow

interface ClassRepository {

    fun fetchAllClass(schoolId: String): Flow<Resource<List<ClassDomain>>>

    fun deleteClass(id: String): Flow<Resource<Unit>>
}