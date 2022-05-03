package com.example.data.data.mappers

import com.example.data.data.models.StudentData
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.StudentDomain
import com.example.domain.domain.models.StudentImageDomain

class StudentDataToDomainMapper : Mapper<StudentData, StudentDomain>() {
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
            image = StudentImageDomain(name = image.name, type = image.type, url = image.url))
    }
}