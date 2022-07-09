package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.UserModel
import com.example.bookloverfinalapp.app.models.StudentImage
import com.example.domain.Mapper
import com.example.domain.models.StudentDomain

class StudentDomainToUserModelMapper(private val viewHolderType: Int) :
    Mapper<StudentDomain, UserModel> {
    override fun map(from: StudentDomain): UserModel = from.run {
        UserModel(objectId = objectId,
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
            booksId = booksId,
            image = StudentImage(name = image.name, type = image.type, url = image.url),
            sessionToken = sessionToken, viewHolderType = viewHolderType)
    }
}