package com.example.domain.repository

import com.example.domain.Resource
import com.example.domain.models.StudentDomain
import com.example.domain.models.UpdateAnswerDomain
import com.example.domain.models.UserDomain
import com.example.domain.models.UserUpdateDomain
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun fetchMyStudents(classId: String): Flow<Resource<List<StudentDomain>>>

    fun fetchSchoolStudents(schoolId: String): Flow<Resource<List<StudentDomain>>>

    fun onRefresh(classId: String): Flow<Resource<List<StudentDomain>>>

    fun fetchClassUsers(classId: String): Flow<Resource<List<StudentDomain>>>

    fun updateStudentClass(
        id: String,
        sessionToken: String,
        classId: String,
        classTitle: String,
    ): Flow<Resource<Unit>>

    fun updateUser(
        id: String,
        user: UserUpdateDomain,
        sessionToken: String,
    ): Flow<Resource<UpdateAnswerDomain>>

    fun deleteUser(id: String, sessionToken: String): Flow<Resource<Unit>>

    fun addSessionToken(id: String, sessionToken: String): Flow<Resource<Unit>>

    fun getCurrentUser(sessionToken: String): Flow<Resource<UserDomain>>

    suspend fun clearStudentsCache()

}