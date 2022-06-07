package com.example.data.cache.mappers

import com.example.data.cache.models.ClassCache
import com.example.data.models.ClassData
import com.example.domain.Mapper

class ClassCacheToDataMapper : Mapper<ClassCache, ClassData> {
    override fun map(from: ClassCache): ClassData = from.run {
        ClassData(objectId = id, title = title, schoolId = schoolId)
    }

}