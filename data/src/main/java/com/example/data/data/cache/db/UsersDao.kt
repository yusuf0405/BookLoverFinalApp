package com.example.data.data.cache.db

import androidx.room.*
import com.example.data.data.cache.models.StudentDb

@Dao
interface UsersDao {
    @Insert
    suspend fun addNewUser(user: StudentDb)

    @Update
    suspend fun updateUser(user: StudentDb)

    @Query("DELETE FROM users_database")
    fun clearTable()

    @Delete
    suspend fun deleteUser(user: StudentDb)

    @Query("select * from users_database")
    suspend fun getAllUsers(): MutableList<StudentDb>

    @Query("select * from users_database where objectId == :id")
    suspend fun getUser(id: String): StudentDb
}