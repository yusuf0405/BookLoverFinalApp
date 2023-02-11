package com.example.data.mappers

import com.example.data.models.TaskData
import com.example.domain.Mapper
import com.example.domain.models.TaskDomain
import javax.inject.Inject

class TaskDataToDomainMapper @Inject constructor() : Mapper<TaskData, TaskDomain> {

    override fun map(from: TaskData): TaskDomain = from.run {
        TaskDomain(
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