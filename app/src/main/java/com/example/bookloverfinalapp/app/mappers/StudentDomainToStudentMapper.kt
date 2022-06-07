package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.models.StudentImage
import com.example.domain.Mapper
import com.example.domain.models.StudentDomain

class StudentDomainToStudentMapper : Mapper<StudentDomain, Student> {
    override fun map(from: StudentDomain): Student = from.run {
        Student(objectId = objectId,
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
            sessionToken = sessionToken)
    }
}