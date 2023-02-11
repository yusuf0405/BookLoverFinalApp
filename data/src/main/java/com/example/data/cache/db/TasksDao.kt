package com.example.data.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.cache.models.AudioBookCache
import com.example.data.cache.models.BookThatReadCache
import com.example.data.cache.models.TaskCache
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Query("select * from TASKS_TABLE")
    fun fetchAllTasksObservable(): Flow<MutableList<TaskCache>>

    @Query("select * from TASKS_TABLE")
    suspend fun fetchAllTasksSingle(): MutableList<TaskCache>

    @Query("select * from TASKS_TABLE where id == :taskId")
    suspend fun fetchTaskFromId(taskId: String): TaskCache

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewTask(task: TaskCache)

    @Query("DELETE FROM AUDIO_BOOKS_TABLE")
    fun clearTable()

}