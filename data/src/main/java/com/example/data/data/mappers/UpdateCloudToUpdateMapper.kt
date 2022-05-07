package com.example.data.data.mappers

import com.example.data.data.cloud.models.UpdateCloud
import com.example.domain.domain.Mapper
import com.example.domain.models.student.UpdateAnswerDomain

class UpdateCloudToUpdateMapper : Mapper<UpdateCloud, UpdateAnswerDomain>() {
    override fun map(from: UpdateCloud): UpdateAnswerDomain = from.run {
        UpdateAnswerDomain(updatedAt = updatedAt)
    }
}