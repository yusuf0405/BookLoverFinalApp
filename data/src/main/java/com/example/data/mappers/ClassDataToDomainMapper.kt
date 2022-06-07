package com.example.data.mappers

import com.example.data.models.ClassData
import com.example.domain.Mapper
import com.example.domain.models.ClassDomain

class ClassDataToDomainMapper : Mapper<ClassData, ClassDomain> {
    override fun map(from: ClassData): ClassDomain = from.run {
        ClassDomain(id = objectId, title = title, schoolId = schoolId)
    }
}