package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.ClassAdapterModel
import com.example.domain.Mapper
import com.example.domain.models.ClassDomain

class ClassDomainToAdapterModelMapper : Mapper<ClassDomain, ClassAdapterModel.Base>() {
    override fun map(from: ClassDomain): ClassAdapterModel.Base = from.run {
        ClassAdapterModel.Base(id = id, title = title, schoolId = schoolId)
    }
}