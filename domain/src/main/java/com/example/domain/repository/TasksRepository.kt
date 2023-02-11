package com.example.domain.repository

import com.example.domain.models.TaskDomain
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    fun fetchAllClassTasks(classId: String): Flow<List<TaskDomain>>
}