package com.example.domain.repository

import com.example.domain.Resource
import com.example.domain.models.StudentDomain
import com.example.domain.models.UpdateAnswerDomain
import com.example.domain.models.UserDomain
import com.example.domain.models.UserUpdateDomain
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun updateUser(
        id: String,
        user: UserUpdateDomain,
        sessionToken: String,
    ): Flow<Resource<UpdateAnswerDomain>>

    fun deleteUser(id: String, sessionToken: String): Flow<Resource<Unit>>

    fun fetchMyStudents(classId: String): Flow<Resource<List<StudentDomain>>>

    fun fetchClassStudents(classId: String): Flow<Resource<List<StudentDomain>>>

    fun addSessionToken(id: String, sessionToken: String): Flow<Resource<Unit>>

    fun getCurrentUser(sessionToken: String): Flow<Resource<UserDomain>>

    suspend fun clearStudentsCache()

}