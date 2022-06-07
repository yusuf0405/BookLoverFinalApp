package com.example.data.cache.mappers

import com.example.data.cache.models.ClassCache
import com.example.data.models.ClassData
import com.example.domain.Mapper

class ClassDataToCacheMapper : Mapper<ClassData, ClassCache> {
    override fun map(from: ClassData): ClassCache = from.run {
        ClassCache(id = objectId, title = title, schoolId = schoolId)
    }
}