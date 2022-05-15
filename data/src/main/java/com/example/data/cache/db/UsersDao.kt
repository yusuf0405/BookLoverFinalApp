package com.example.data.cache.db

import androidx.room.*
import com.example.data.cache.models.StudentDb

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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

    @Query("DELETE FROM users_database WHERE objectId = :id")
    fun deleteByUserId(id: String)

    @Query("select * from users_database where classId == :classId")
    suspend fun getMyUsers(classId: String): MutableList<StudentDb>
}