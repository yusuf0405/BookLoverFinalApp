package com.example.data.cache.source

import com.example.data.cache.db.UsersDao
import com.example.data.cache.models.StudentDb
import com.example.data.models.StudentData
import com.example.domain.Mapper

interface UsersCacheDataSource {

    suspend fun fetchMyStudents(classId: String): List<StudentDb>

    suspend fun saveStudents(users: List<StudentData>)

    suspend fun deleteUser(id: String)

    suspend fun clearStudents()

    class Base(
        private val dao: UsersDao,
        private val dataMapper: Mapper<StudentData, StudentDb>,
    ) :
        UsersCacheDataSource {

        override suspend fun fetchMyStudents(classId: String): List<StudentDb> =
            dao.getMyUsers(classId = classId)

        override suspend fun saveStudents(users: List<StudentData>) {
            users.forEach { userData -> dao.addNewUser(user = dataMapper.map(userData)) }
        }

        override suspend fun deleteUser(id: String) = dao.deleteByUserId(id = id)

        override suspend fun clearStudents() = dao.clearTable()
    }
}