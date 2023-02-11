package com.example.data.cloud.source

import com.example.data.models.TaskData
import kotlinx.coroutines.flow.Flow

interface TasksCloudDataSource {

    fun fetchAllClassTasks(classId: String): Flow<List<TaskData>>
}