package com.example.data.cache.mappers

import com.example.data.cache.models.TaskCache
import com.example.data.models.TaskData
import com.example.domain.Mapper
import javax.inject.Inject

class TaskCacheToDataMapper @Inject constructor() : Mapper<TaskCache, TaskData> {

    override fun map(from: TaskCache): TaskData = from.run {
        TaskData(
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