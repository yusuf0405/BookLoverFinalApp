package com.example.data.cache.mappers

import com.example.data.cache.models.UserCache
import com.example.data.cache.models.UserImageCache
import com.example.data.models.StudentData
import com.example.domain.Mapper

class StudentDataToDbMapper : Mapper<StudentData, UserCache> {
    override fun map(from: StudentData): UserCache = from.run {
        UserCache(
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
            image = UserImageCache(
                name = image.name,
                type = image.type,
                url = image.url
            ),
            booksRead = booksRead,
            progress = progress,
            chaptersRead = chaptersRead,
            booksId = booksId,
            sessionToken = sessionToken,
            schoolId = schoolId
        )
    }
}