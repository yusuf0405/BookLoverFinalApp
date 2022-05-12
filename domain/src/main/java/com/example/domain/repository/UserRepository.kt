package com.example.domain.repository

import com.example.domain.Resource
import com.example.domain.models.StudentDomain
import com.example.domain.models.UpdateAnswerDomain
import com.example.domain.models.UserUpdateDomain
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun updateUser(
        id: String,
        user: UserUpdateDomain,
        sessionToken: String,
    ): Flow<Resource<UpdateAnswerDomain>>

    fun fetchMyStudents(classId: String): Flow<Resource<List<StudentDomain>>>

    suspend fun clearStudentsCache()

}