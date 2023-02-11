package com.example.data.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.cache.models.AudioBookCache
import com.example.data.cache.models.UserCache
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewUser(user: UserCache)

    @Query("select * from users where class_id == :classId")
    fun fetchAllStudentsFromClassIdObservable(classId: String): Flow<MutableList<UserCache>>

    @Query("select * from users where objectId == :userId")
    suspend fun fetchUserFromId(userId: String): UserCache

    @Query("select * from users where class_id == :classId")
    fun fetchAllStudentsFromClassIdSingle(classId: String): MutableList<UserCache>

    @Query("select * from users where school_id == :schoolId")
    fun fetchAllStudentsFromSchoolObservables(schoolId: String): Flow<MutableList<UserCache>>

    @Query("select * from users where school_id == :schoolId")
    fun fetchAllStudentsFromSchoolIdSingle(schoolId: String): MutableList<UserCache>

    @Query("DELETE FROM users WHERE objectId = :id")
    fun deleteByUserId(id: String)

    @Query("DELETE FROM users")
    fun clearTable()

}