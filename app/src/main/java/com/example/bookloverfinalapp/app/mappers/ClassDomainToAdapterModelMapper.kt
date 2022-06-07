package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.ui.adapter.SchoolClassModel
import com.example.domain.Mapper
import com.example.domain.models.ClassDomain

class ClassDomainToAdapterModelMapper : Mapper<ClassDomain, SchoolClassModel> {
    override fun map(from: ClassDomain): SchoolClassModel = from.run {
        SchoolClassModel(id = id, title = title, schoolId = schoolId)
    }
}