package com.example.data.cache.source.users

import com.example.data.cache.models.UserCache
import com.example.data.models.StudentData
import kotlinx.coroutines.flow.Flow

interface UsersCacheDataSource {

    fun fetchAllStudentsFromClassIdObservable(classId: String): Flow<List<UserCache>>

    suspend fun fetchAllStudentsFromClassIdSingle(classId: String): List<UserCache>

    suspend fun fetchUserFromId(userId: String): UserCache

    fun fetchAllStudentsFromSchoolIdObservable(schoolId: String): Flow<List<UserCache>>

    suspend fun fetchAllStudentsFromSchoolIdSingle(schoolId: String): List<UserCache>

    suspend fun saveStudents(users: List<StudentData>)

    suspend fun deleteUser(id: String)

    suspend fun clearStudents()
}