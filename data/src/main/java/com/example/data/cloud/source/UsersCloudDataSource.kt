package com.example.data.cloud.source

import com.example.data.cloud.CloudDataRequestState
import com.example.data.cloud.models.UserCloud
import com.example.data.models.StudentData
import com.example.data.repository.StudentsResponseType
import com.example.domain.models.UpdateAnswerDomain
import com.example.domain.models.UserUpdateDomain
import kotlinx.coroutines.flow.Flow

interface UsersCloudDataSource {

    fun fetchAllUsersFromId(
        responseType: StudentsResponseType,
        schoolId: String,
    ): Flow<List<StudentData>>

    fun fetchAllUsersFromUsersId(
        usersId: List<String>,
        schoolId: String,
    ): Flow<List<UserCloud>>

    suspend fun updateUser(
        id: String,
        user: UserUpdateDomain,
        sessionToken: String,
    ): CloudDataRequestState<UpdateAnswerDomain>

    suspend fun updateClass(
        id: String,
        sessionToken: String,
        classId: String,
        classTitle: String,
    ): CloudDataRequestState<Unit>

    suspend fun deleteUser(id: String, sessionToken: String): CloudDataRequestState<Unit>

    suspend fun addSessionToken(id: String, sessionToken: String): CloudDataRequestState<Unit>

    fun getCurrentUser(sessionToken: String): Flow<UserCloud>
}