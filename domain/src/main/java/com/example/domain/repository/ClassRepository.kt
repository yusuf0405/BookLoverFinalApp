package com.example.domain.repository

import com.example.domain.RequestState
import com.example.domain.Resource
import com.example.domain.models.ClassDomain
import com.example.domain.models.PostRequestAnswerDomain
import kotlinx.coroutines.flow.Flow

interface ClassRepository {

    fun fetchAllClass(schoolId: String): Flow<List<ClassDomain>>

    fun fetchAllClassCloud(schoolId: String): Flow<List<ClassDomain>>

    suspend fun deleteClass(id: String): RequestState<Unit>

    suspend fun addClass(title: String, schoolId: String): RequestState<Unit>

    fun fetchUserClassesFromId(id: String): Flow<List<ClassDomain>>

    suspend fun clearTable()

}