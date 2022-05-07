package com.example.data.data.cloud.mappers

import com.example.data.data.cloud.models.SignUpAnswerCloud
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.UserDomainImage
import com.example.domain.models.student.PostRequestAnswerDomain

class PostRequestAnswerToAnswerMapper : Mapper<SignUpAnswerCloud, PostRequestAnswerDomain>() {
    override fun map(from: SignUpAnswerCloud): PostRequestAnswerDomain = from.run {
        PostRequestAnswerDomain(id = objectId,
            createdAt = createdAt,
            sessionToken = sessionToken,
            image = UserDomainImage(name = image.name, type = image.type, url = image.url))
    }
}