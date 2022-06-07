package com.example.data.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.cache.models.ClassCache

@Dao
interface ClassDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewClass(schoolClass: ClassCache)

    @Query("select * from school_classes")
    suspend fun getAllClass(): MutableList<ClassCache>

    @Query("DELETE FROM school_classes WHERE id = :id")
    fun deleteByClassId(id: String)

    @Query("DELETE FROM school_classes")
    fun clearTable()
}
