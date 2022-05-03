package com.example.bookloverfinalapp.app.ui.mappers

import com.example.bookloverfinalapp.app.models.StudentAdapterModel
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.StudentDomain

class StudentAdapterModelMapper : Mapper<StudentDomain, StudentAdapterModel.Base>() {
    override fun map(from: StudentDomain): StudentAdapterModel.Base = from.run {
        StudentAdapterModel.Base(objectId = objectId,
            classId = classId,
            createAt = createAt,
            schoolName = schoolName,
            className = className,
            email = email,
            gender = gender,
            lastname = lastname,
            name = name,
            number = number,
            userType = userType,
            chaptersRead = chaptersRead,
            booksRead = booksRead,
            progress = progress,
            booksId = booksId)
    }
}