package com.example.data.cache.db

import androidx.room.*
import com.example.data.cache.models.ClassCache

@Dao
interface ClassDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewClass(schoolClass: ClassCache)

    @Update
    suspend fun updateClass(schoolClass: ClassCache)

    @Query("DELETE FROM class_database")
    fun clearTable()

    @Query("DELETE FROM class_database WHERE id = :id")
    fun deleteByClassId(id: String)

    @Delete
    suspend fun deleteClass(schoolClass: ClassCache)

    @Query("select * from class_database")
    suspend fun getAllClass(): MutableList<ClassCache>

    @Query("select * from class_database where id == :id")
    suspend fun getClass(id: String): ClassCache
}
