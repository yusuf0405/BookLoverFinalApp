package com.example.domain.repository

import com.example.domain.Resource
import com.example.domain.models.ClassDomain
import com.example.domain.models.SchoolDomain
import kotlinx.coroutines.flow.Flow

interface SchoolRepository {

    fun getAllClasses(): Flow<Resource<List<ClassDomain>>>

    fun getAllSchools(): Flow<Resource<List<SchoolDomain>>>

    fun getClass(id: String): Flow<Resource<List<ClassDomain>>>

}