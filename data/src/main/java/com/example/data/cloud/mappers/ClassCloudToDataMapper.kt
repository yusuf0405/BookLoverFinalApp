package com.example.data.cloud.mappers

import com.example.data.cloud.models.ClassCloud
import com.example.data.models.ClassData
import com.example.domain.Mapper

class ClassCloudToDataMapper : Mapper<ClassCloud, ClassData> {
    override fun map(from: ClassCloud): ClassData = from.run {
        ClassData(objectId = objectId, title = title, schoolId = schoolId)
    }
}