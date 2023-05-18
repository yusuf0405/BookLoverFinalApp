package com.example.domain.repository

import com.example.domain.RequestState
import com.example.domain.Resource
import com.example.domain.models.StudentDomain
import com.example.domain.models.UpdateAnswerDomain
import com.example.domain.models.UserDomain
import com.example.domain.models.UserUpdateDomain
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun fetchUserInfoFromCloud(sessionToken: String): Flow<UserDomain>

    fun fetchAllStudentsFromClassId(
        classId: String,
        schoolId: String,
        currentUserId: String
    ): Flow<List<StudentDomain>>

    fun fetchAllUsersFromBookId(
        bookId: String,
        schoolId: String,
        currentUserId: String
    ): Flow<List<UserDomain>>


    fun fetchSchoolStudents(schoolId: String): Flow<List<StudentDomain>>

    suspend fun fetchUserFromId(userId: String): StudentDomain

    fun onRefresh(classId: String): Flow<Resource<List<StudentDomain>>>

    suspend fun updateStudentClass(
        id: String,
        sessionToken: String,
        classId: String,
        classTitle: String,
    ): RequestState<Unit>

    suspend fun updateUserParameters(
        id: String,
        user: UserUpdateDomain,
        sessionToken: String,
    ): RequestState<UpdateAnswerDomain>

    suspend fun deleteUser(id: String, sessionToken: String): RequestState<Unit>

    suspend fun addSessionToken(id: String, sessionToken: String): RequestState<Unit>

    fun getCurrentUserWithCloud(sessionToken: String): Flow<UserDomain>

    suspend fun clearStudentsCache()

}