package com.example.data

import com.example.data.data.models.UserData
import com.example.data.data.models.UserImageData
import com.example.data.models.classes.ClassDto
import com.example.data.models.school.SchoolDto
import com.example.data.models.student.*
import com.example.domain.domain.models.UserDomain
import com.example.domain.domain.models.UserDomainImage
import com.example.domain.models.classes.Class
import com.example.domain.models.school.School
import com.example.domain.models.student.*
import com.parse.ParseFile

internal fun UserData.toUser(): UserDomain =
    UserDomain(
        createAt = createAt,
        classId = classId,
        email = email,
        gender = gender,
        lastname = lastname,
        name = name,
        image = UserDomainImage(name = image.name, type = image.type, url = image.url),
        number = number,
        schoolName = schoolName,
        className = className,
        id = objectId,
        userType = userType,
        sessionToken = sessionToken
    )

internal fun UserData.toStudentNoImage(): UserDomain =
    UserDomain(
        createAt = createAt,
        classId = classId,
        email = email,
        gender = gender,
        lastname = lastname,
        name = name,
        number = number,
        schoolName = schoolName,
        className = className,
        id = objectId,
        userType = userType, sessionToken = sessionToken
    )

internal fun StudentAddBook.toRequestBook(): StudentAddBookRequest =
    StudentAddBookRequest(books = books)

internal fun StudentAddBookRequest.toBook(): StudentAddBook =
    StudentAddBook(books = books)


internal fun UserSignUpRes.toDtoSignUp(): UserSignUpRequest =
    UserSignUpRequest(
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
        username = email
    )


internal fun StudentUpdateRes.toDtoStudent(): UserUpdateRequest =
    UserUpdateRequest(
        image = image.toDtoImage(),
        lastname = lastname,
        name = name,
        gender = gender,
        email = email,
        number = number
    )


internal fun UpdateAnswer.toDtoUpdate(): UpdateDto =
    UpdateDto(
        updatedAt = updatedAt
    )

internal fun UpdateDto.toDtoUpdate(): UpdateAnswer =
    UpdateAnswer(
        updatedAt = updatedAt
    )

internal fun ParseFile.toImage(): UserDomainImage =
    UserDomainImage(
        name = name,
        type = "File",
        url = url,
    )


private fun UserImageData.toImage(): UserDomainImage =
    UserDomainImage(
        name = name,
        url = url,
        type = type
    )

private fun UserDomainImage.toDtoImage(): UserImageData =
    UserImageData(
        name = name,
        url = url,
        type = type
    )

internal fun ClassDto.toClass(): Class =
    Class(
        objectId = objectId,
        teachers = teachers,
        title = title,
        students = students
    )


internal fun SchoolDto.toSchool(): School =
    School(
        objectId = objectId,
        title = title,
        classesIds = classes,
    )

internal fun PostRequestAnswerDto.toRequestAnswer(): PostRequestAnswer =
    PostRequestAnswer(
        id = objectId,
        createdAt = createdAt,
        sessionToken = sessionToken,
        image = UserDomainImage(name = image.name, type = image.type, url = image.url)
    )

internal fun School.toSchoolDto(): SchoolDto =
    SchoolDto(
        objectId = objectId,
        title = title,
        classes = classesIds,
    )

