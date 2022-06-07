package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.domain.Mapper
import com.example.domain.models.ClassDomain

class ClassDomainToClassMapper : Mapper<ClassDomain, SchoolClass> {
    override fun map(from: ClassDomain): SchoolClass = from.run {
        SchoolClass(id = id, title = title, schoolId = schoolId)
    }
}