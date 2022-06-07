package com.example.data.cache.mappers

import com.example.data.cache.models.UserCache
import com.example.data.models.StudentData
import com.example.data.models.StudentImageData
import com.example.domain.Mapper

class UserDbToDataMapper : Mapper<UserCache, StudentData> {
    override fun map(from: UserCache): StudentData = from.run {
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
            booksId = booksId,
            sessionToken = sessionToken)
    }
}