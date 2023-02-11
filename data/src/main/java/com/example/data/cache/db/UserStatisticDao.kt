package com.example.data.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.cache.models.UserStatisticCache

@Dao
interface UserStatisticDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNewStatistics(userStatistics: List<UserStatisticCache>)

    @Query("select * from USER_STATISTICS")
    fun fetchAllStatisticDays(): MutableList<UserStatisticCache>


    @Query("DELETE FROM USER_STATISTICS")
    fun clearTable()
}
