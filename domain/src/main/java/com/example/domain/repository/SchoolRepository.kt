package com.example.domain.repository

import com.example.domain.Resource
import com.example.domain.models.SchoolDomain
import kotlinx.coroutines.flow.Flow

interface SchoolRepository {

    fun getAllSchools(): Flow<Resource<List<SchoolDomain>>>

}