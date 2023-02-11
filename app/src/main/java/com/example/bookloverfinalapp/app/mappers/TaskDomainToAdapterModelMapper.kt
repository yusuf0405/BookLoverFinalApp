package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.TaskAdapterModel
import com.example.domain.Mapper
import com.example.domain.models.TaskDomain
import javax.inject.Inject

class TaskDomainToAdapterModelMapper @Inject constructor() : Mapper<TaskDomain, TaskAdapterModel> {

    override fun map(from: TaskDomain): TaskAdapterModel =from.run {
        TaskAdapterModel(
            id = id,
            title = title,
            description = description,
            startDate = startDate,
            endDate = endDate,
            taskGenres = taskGenres,
            classId = classId
        )
    }
}