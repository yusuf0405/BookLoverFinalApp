package com.example.domain.domain.repository

import com.example.domain.domain.models.StudentDomain
import com.example.domain.models.Resource
import com.example.domain.models.student.UserUpdateDomain
import com.example.domain.models.student.UpdateAnswerDomain
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun updateUser(
        id: String,
        user: UserUpdateDomain,
        sessionToken: String,
    ): Flow<Resource<UpdateAnswerDomain>>

    fun fetchMyStudents(className: String, schoolName: String): Flow<Resource<List<StudentDomain>>>

    suspend fun clearStudentsCache()

}