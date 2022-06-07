package com.example.data.mappers

import com.example.data.models.StudentData
import com.example.domain.Mapper
import com.example.domain.models.StudentDomain
import com.example.domain.models.StudentImageDomain

class StudentDataToDomainMapper : Mapper<StudentData, StudentDomain> {
    override fun map(from: StudentData): StudentDomain = from.run {
        StudentDomain(
            objectId = objectId,
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
            image = StudentImageDomain(name = image.name, type = image.type, url = image.url),
            sessionToken = sessionToken)
    }
}