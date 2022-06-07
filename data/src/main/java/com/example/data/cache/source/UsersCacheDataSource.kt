package com.example.data.cache.source

import com.example.data.cache.db.UsersDao
import com.example.data.cache.models.UserCache
import com.example.data.models.StudentData
import com.example.domain.Mapper

interface UsersCacheDataSource {

    suspend fun fetchMyStudents(classId: String): List<UserCache>

    suspend fun saveStudents(users: List<StudentData>)

    suspend fun deleteUser(id: String)

    suspend fun clearStudents()

    class Base(
        private val dao: UsersDao,
        private val dataMapper: Mapper<StudentData, UserCache>,
    ) :
        UsersCacheDataSource {

        override suspend fun fetchMyStudents(classId: String): List<UserCache> =
            dao.getMyUsers(classId = classId)

        override suspend fun saveStudents(users: List<StudentData>) {
            users.forEach { userData -> dao.addNewUser(user = dataMapper.map(userData)) }
        }

        override suspend fun deleteUser(id: String) = dao.deleteByUserId(id = id)

        override suspend fun clearStudents() = dao.clearTable()
    }
}