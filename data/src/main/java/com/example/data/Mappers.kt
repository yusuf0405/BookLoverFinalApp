package com.example.data

import com.example.data.cloud.models.SchoolCloud
import com.example.data.cloud.models.UserSignUpCloud
import com.example.data.models.BookData
import com.example.data.models.BookPdfData
import com.example.data.models.BookPosterData
import com.example.domain.models.AddNewBookDomain
import com.example.domain.models.SchoolDomain
import com.example.domain.models.UserSignUpDomain
import java.util.*


internal fun UserSignUpDomain.toDtoSignUp(): UserSignUpCloud =
    UserSignUpCloud(
        name = name,
        lastname = lastname,
        email = email,
        password = password,
        number = number,
        className = className,
        schoolName = schoolName,
        gender = gender,
        classId = classId,
        userType = userType,
        username = email,
        schoolId = schoolId,
        userSessionToken = "null"
    )

internal fun AddNewBookDomain.mapToBook(id: String, createdAt: Date): BookData = BookData(
    id = id,
    createdAt = createdAt,
    book = BookPdfData(name = book.name, url = book.url, type = book.type),
    author = author,
    poster = BookPosterData(name = poster.name, url = poster.url),
    updatedAt = createdAt,
    chapterCount = chapterCount,
    page = page,
    publicYear = publicYear,
    title = title,
    genres = genres,
    description = description,
    isExclusive = isExclusive
)

internal fun SchoolCloud.toSchool(): SchoolDomain =
    SchoolDomain(id = objectId, title = title)
