package com.example.data.cloud.mappers

import com.example.data.cloud.models.TaskCloud
import com.example.data.models.TaskData
import com.example.domain.Mapper
import java.util.*
import javax.inject.Inject

class TaskCloudToDataMapper @Inject constructor() : Mapper<TaskCloud, TaskData> {

    override fun map(from: TaskCloud): TaskData = from.run {
        TaskData(
            id = id,
            title = title,
            description = description,
            classId = classId,
            startDate = Date(),
            endDate = Date(),
            taskGenres = taskGenres
        )
    }
}