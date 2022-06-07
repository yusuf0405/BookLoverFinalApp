package com.example.data.mappers

import com.example.data.cloud.models.UpdateCloud
import com.example.domain.Mapper
import com.example.domain.models.UpdateAnswerDomain

class UpdateCloudToUpdateMapper : Mapper<UpdateCloud, UpdateAnswerDomain> {
    override fun map(from: UpdateCloud): UpdateAnswerDomain = from.run {
        UpdateAnswerDomain(updatedAt = updatedAt)
    }
}