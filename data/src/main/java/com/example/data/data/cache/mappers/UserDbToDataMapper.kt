package com.example.data.data.cache.mappers

import com.example.data.data.cache.models.StudentDb
import com.example.data.data.models.StudentData
import com.example.data.data.models.StudentImageData
import com.example.domain.domain.Mapper

class UserDbToDataMapper : Mapper<StudentDb, StudentData>() {
    override fun map(from: StudentDb): StudentData = from.run {
        StudentData(
            createAt = createAt,
            classId = classId,
            email = email,
            gender = gender,
            lastname = lastname,
            name = name,
            number = number,
            schoolName = schoolName,
            className = className,
            objectId = objectId,
            userType = userType,
            image = StudentImageData(name = image.name, type = image.type, url = image.url),
            chaptersRead = chaptersRead,
            booksRead = booksRead,
            progress = progress,
            booksId = booksId)
    }
}