package com.example.data.repository.school_classes

import com.example.data.cache.models.ClassCache
import com.example.data.cache.source.classes.ClassCacheDataSource
import com.example.data.cloud.source.school_classes.ClassCloudDataSource
import com.example.data.models.ClassData
import com.example.data.repository.BaseRepository
import com.example.domain.Mapper
import com.example.domain.models.ClassDomain
import com.example.domain.repository.ClassRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class ClassRepositoryImpl(
    private val cloudDataSource: ClassCloudDataSource,
    private val cacheDataSource: ClassCacheDataSource,
    private val classMapper: Mapper<ClassData, ClassDomain>,
    private val classCashMapper: Mapper<ClassCache, ClassData>,
) : ClassRepository, BaseRepository {

    override fun fetchAllClass(schoolId: String): Flow<List<ClassDomain>> =
        cacheDataSource.fetchAllClass()
            .filter { it.isNotEmpty() }
            .flowOn(Dispatchers.Default)
            .map { it.map(classCashMapper::map) }
            .onEach(cacheDataSource::saveClasses)
            .map { it.map(classMapper::map) }
            .flowOn(Dispatchers.IO)

    override fun fetchAllClassCloud(schoolId: String): Flow<List<ClassDomain>> =
        cloudDataSource.getAllClass(schoolId = schoolId)
            .filter { it.isNotEmpty() }
            .flowOn(Dispatchers.Default)
            .onEach(cacheDataSource::saveClasses)
            .map { it.map(classMapper::map) }
            .flowOn(Dispatchers.IO)

    override fun fetchUserClassesFromId(id: String): Flow<List<ClassDomain>> =
        cloudDataSource.fetchUserClassesFromId(id = id)
            .map { classes -> classes.map(classMapper::map) }
            .flowOn(Dispatchers.Default)

    override suspend fun deleteClass(id: String) = renderResultToUnit(
        result = cloudDataSource.deleteClass(id = id),
        onSuccess = { cacheDataSource.deleteClass(id = id) }
    )

    override suspend fun addClass(
        title: String,
        schoolId: String
    ) = renderResultToUnit(
        result = cloudDataSource.addClass(title = title, schoolId = schoolId),
        onSuccess = { data ->
            val classData = ClassData(
                objectId = data.objectId,
                title = title,
                schoolId = schoolId
            )
            cacheDataSource.addClass(classData)
        }
    )


    override suspend fun clearTable() = cacheDataSource.clearTable()

}
