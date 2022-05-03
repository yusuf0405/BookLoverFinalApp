package com.example.data.data.cache.source

import com.example.data.data.cache.db.UsersDao
import com.example.data.data.cache.models.StudentDb
import com.example.data.data.models.StudentData
import com.example.domain.domain.Mapper

interface UsersCacheDataSource {

    suspend fun fetchMyStudents(): List<StudentDb>

    suspend fun saveUsers(users: List<StudentData>)

    class Base(
        private val dao: UsersDao,
        private val dataMapper: Mapper<StudentData, StudentDb>,
    ) :
        UsersCacheDataSource {

        override suspend fun fetchMyStudents(): List<StudentDb> = dao.getAllUsers()

        override suspend fun saveUsers(users: List<StudentData>) {
            users.forEach { userData -> dao.addNewUser(user = dataMapper.map(userData)) }
        }
    }
}