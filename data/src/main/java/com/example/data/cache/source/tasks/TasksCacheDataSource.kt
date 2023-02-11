package com.example.data.cache.source.tasks

import com.example.data.models.TaskData
import kotlinx.coroutines.flow.Flow

interface TasksCacheDataSource {

    suspend fun fetchAllClassTasksSingle(): List<TaskData>

    fun fetchAllClassTasksObservable(): Flow<List<TaskData>>

    suspend fun saveNewTaskInCache(task: TaskData)
}