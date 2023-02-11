package com.example.data.cache.source.tasks

import com.example.data.cache.db.TasksDao
import com.example.data.cache.models.TaskCache
import com.example.data.models.TaskData
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TasksCacheDataSourceImpl @Inject constructor(
    private val dao: TasksDao,
    private val dispatchersProvider: DispatchersProvider,
    private val taskCacheToDataMapper: Mapper<TaskCache, TaskData>,
    private val taskDataToCacheMapper: Mapper<TaskData, TaskCache>
) : TasksCacheDataSource {

    override suspend fun fetchAllClassTasksSingle(): List<TaskData> =
        dao.fetchAllTasksSingle().map(taskCacheToDataMapper::map)

    override fun fetchAllClassTasksObservable(): Flow<List<TaskData>> =
        dao.fetchAllTasksObservable()
            .flowOn(dispatchersProvider.io())
            .map { tasks -> tasks.map(taskCacheToDataMapper::map) }
            .flowOn(dispatchersProvider.default())

    override suspend fun saveNewTaskInCache(task: TaskData) {
        dao.addNewTask(task = taskDataToCacheMapper.map(task))
    }
}