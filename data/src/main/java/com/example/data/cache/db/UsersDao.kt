package com.example.data.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.cache.models.UserCache

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewUser(user: UserCache)

    @Query("select * from users where class_id == :classId")
    suspend fun getMyUsers(classId: String): MutableList<UserCache>

    @Query("DELETE FROM users WHERE objectId = :id")
    fun deleteByUserId(id: String)

    @Query("DELETE FROM users")
    fun clearTable()

}