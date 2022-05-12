package com.example.data.cloud.mappers

import com.example.data.cloud.models.SignUpAnswerCloud
import com.example.domain.Mapper
import com.example.domain.models.UserDomainImage
import com.example.domain.models.PostRequestAnswerDomain

class PostRequestAnswerToAnswerMapper : Mapper<SignUpAnswerCloud, PostRequestAnswerDomain>() {
    override fun map(from: SignUpAnswerCloud): PostRequestAnswerDomain = from.run {
        PostRequestAnswerDomain(id = objectId,
            createdAt = createdAt,
            sessionToken = sessionToken,
            image = UserDomainImage(name = image.name, type = image.type, url = image.url))
    }
}