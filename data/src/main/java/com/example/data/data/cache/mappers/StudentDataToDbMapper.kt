package com.example.data.data.cache.mappers

import com.example.data.data.cache.models.StudentDb
import com.example.data.data.cache.models.StudentImageDb
import com.example.data.data.models.StudentData
import com.example.domain.domain.Mapper

class StudentDataToDbMapper : Mapper<StudentData, StudentDb>() {
    override fun map(from: StudentData): StudentDb = from.run {
        StudentDb(
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
            image = StudentImageDb(name = image?.name ?: "",
                type = image?.type ?: "",
                url = image?.url ?: ""),
            booksRead = booksRead,
            progress = progress,
            chaptersRead = chaptersRead,
            booksId = booksId,
        )
    }
}