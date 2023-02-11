package com.example.data.mappers

import com.example.data.cache.models.TaskCache
import com.example.data.models.TaskData
import com.example.domain.Mapper
import com.example.domain.models.TaskDomain
import javax.inject.Inject

class TaskDataToCacheMapper @Inject constructor() : Mapper<TaskData, TaskCache> {

    override fun map(from: TaskData): TaskCache = from.run {
        TaskCache(
            id = id,
            title = title,
            description = description,
            classId = classId,
            startDate = startDate,
            endDate = endDate,
            taskGenres = taskGenres
        )
    }
}