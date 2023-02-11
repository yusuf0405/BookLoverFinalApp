package com.example.data.repository

import com.example.data.ResourceProvider
import com.example.data.cloud.service.SchoolService
import com.example.data.toSchool
import com.example.domain.Resource
import com.example.domain.repository.SchoolRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class SchoolRepositoryImpl(
    private val service: SchoolService,
    private val resourceProvider: ResourceProvider,
) : SchoolRepository {

    override fun getAllSchools() = flow {
        emit(service.getAllSchools())
    }.flowOn(Dispatchers.IO)
        .map { dto -> dto.body()!!.schools.map { it.toSchool() } }
        .map { list -> Resource.success(data = list) }
        .onStart { emit(Resource.loading()) }
        .catch { ex -> emit(Resource.error(message = resourceProvider.fetchErrorMessage(ex))) }
        .flowOn(Dispatchers.Default)

}