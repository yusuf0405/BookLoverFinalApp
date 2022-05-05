package com.example.domain.domain.repository

import com.example.domain.models.Resource
import com.example.domain.models.classes.Class
import com.example.domain.models.school.School
import kotlinx.coroutines.flow.Flow

interface SchoolRepository {

    fun getAllClasses(): Flow<Resource<List<Class>>>

    fun getAllSchools(): Flow<Resource<List<School>>>

    fun getClass(id: String): Flow<Resource<List<Class>>>

}