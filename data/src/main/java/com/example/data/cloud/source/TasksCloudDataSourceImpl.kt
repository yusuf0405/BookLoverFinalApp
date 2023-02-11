package com.example.data.cloud.source

import com.example.data.cloud.models.TaskCloud
import com.example.data.cloud.models.TaskResponse
import com.example.data.cloud.service.TaskService
import com.example.data.models.TaskData
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TasksCloudDataSourceImpl @Inject constructor(
    private val service: TaskService,
    private val dispatchersProvider: DispatchersProvider,
    private val taskCloudToTaskDataMapper: Mapper<TaskCloud, TaskData>
) : TasksCloudDataSource {

    override fun fetchAllClassTasks(classId: String): Flow<List<TaskData>> = flow {
        emit(service.fetchAllAudioBooks(id = "{\"classId\":\"${classId}\"}"))
    }.flowOn(dispatchersProvider.io())
        .map { it.body() ?: TaskResponse(emptyList()) }
        .map { it.tasks }
        .map { tasks -> tasks.map(taskCloudToTaskDataMapper::map) }
        .flowOn(dispatchersProvider.default())
}