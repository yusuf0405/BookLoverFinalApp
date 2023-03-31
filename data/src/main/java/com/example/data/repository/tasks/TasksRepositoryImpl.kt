package com.example.data.repository.tasks

import com.example.data.cache.source.tasks.TasksCacheDataSource
import com.example.data.cloud.source.tasks.TasksCloudDataSource
import com.example.data.models.TaskData
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.TaskDomain
import com.example.domain.repository.TasksRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val cloudDataSource: TasksCloudDataSource,
    private val cacheDataSource: TasksCacheDataSource,
    private val dispatchersProvider: DispatchersProvider,
    private val taskDataToDomainMapper: Mapper<TaskData, TaskDomain>
) : TasksRepository {

    override fun fetchAllClassTasks(classId: String): Flow<List<TaskDomain>> = flow {
        emit(cacheDataSource.fetchAllClassTasksSingle())
    }.flatMapLatest { tasks -> handleFetchingTasks(tasksInCache = tasks, classId = classId) }
        .map { tasks -> tasks.map(taskDataToDomainMapper::map) }
        .flowOn(dispatchersProvider.default())


    private fun handleFetchingTasks(
        tasksInCache: List<TaskData>,
        classId: String
    ): Flow<List<TaskData>> =
        if (tasksInCache.isEmpty()) cloudDataSource
            .fetchAllClassTasks(classId = classId)
            .onEach { tasks -> tasks.forEach { cacheDataSource.saveNewTaskInCache(it) } }
        else cacheDataSource.fetchAllClassTasksObservable()
}