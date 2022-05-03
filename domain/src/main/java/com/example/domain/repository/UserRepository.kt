package com.example.domain.repository

import com.example.domain.domain.models.StudentDomain
import com.example.domain.models.Resource
import com.example.domain.models.student.StudentUpdateRes
import com.example.domain.models.student.UpdateAnswer
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun updateUser(
        id: String,
        student: StudentUpdateRes,
        sessionToken: String,
    ): Flow<Resource<UpdateAnswer>>

    fun fetchMyStudents(className: String, schoolName: String): Flow<Resource<List<StudentDomain>>>

}