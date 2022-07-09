package com.example.data.repository

import com.example.data.cache.models.ClassCache
import com.example.data.cache.source.ClassCacheDataSource
import com.example.data.cloud.source.ClassCloudDataSource
import com.example.data.models.ClassData
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.models.ClassDomain
import com.example.domain.repository.ClassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ClassRepositoryImpl(
    private val cloudDataSource: ClassCloudDataSource,
    private val cacheDataSource: ClassCacheDataSource,
    private val classMapper: Mapper<ClassData, ClassDomain>,
    private val classCashMapper: Mapper<ClassCache, ClassData>,
) : ClassRepository {

    override fun fetchAllClass(schoolId: String): Flow<Resource<List<ClassDomain>>> = flow {
        emit(Resource.loading())
        val classCacheList = cacheDataSource.fetchAllClass()
        if (classCacheList.isEmpty()) {
            val result = cloudDataSource.getAllClass(schoolId = schoolId)
            if (result.status == Status.SUCCESS) {
                val classData = result.data!!
                if (classData.isEmpty()) emit(Resource.empty())
                else {
                    cacheDataSource.saveClasses(classes = classData)
                    val classDomain = classData.map { classData -> classMapper.map(classData) }
                    classDomain.lastIndex
                    emit(Resource.success(data = classDomain))
                }
            } else emit(Resource.error(message = result.message))
        } else {
            val classData = classCacheList.map { classCache -> classCashMapper.map(classCache) }
            val classDomain = classData.map { bookData -> classMapper.map(bookData) }
            if (classDomain.isEmpty()) emit(Resource.empty())
            else emit(Resource.success(data = classDomain))
        }
    }

    override fun fetchAllClassCloud(schoolId: String): Flow<Resource<List<ClassDomain>>> = flow {
        val result = cloudDataSource.getAllClass(schoolId = schoolId)
        if (result.status == Status.SUCCESS) {
            val classData = result.data!!
            if (classData.isEmpty()) emit(Resource.empty())
            else {
                cacheDataSource.saveClasses(classes = classData)
                val classDomain = classData.map { classData -> classMapper.map(classData) }
                emit(Resource.success(data = classDomain))
            }
        } else emit(Resource.error(message = result.message))
    }

    override fun getClass(id: String): Flow<Resource<Unit>> = cloudDataSource.getClass(id = id)

    override fun deleteClass(id: String): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.deleteClass(id = id)
        if (result.status == Status.SUCCESS) {
            cacheDataSource.deleteClass(id = id)
            emit(Resource.success(data = Unit))
        } else emit(Resource.error(message = result.message))

    }

    override fun addClass(title: String, schoolId: String): Flow<Resource<String>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.addClass(title = title, schoolId = schoolId)
        if (result.status == Status.SUCCESS) {
            val id = result.data!!.objectId
            cacheDataSource.addClass(ClassData(
                objectId = id,
                title = title,
                schoolId = schoolId))
            emit(Resource.success(data = id))
        } else emit(Resource.error(message = result.message))
    }

    override suspend fun clearTable() = cacheDataSource.clearTable()

}
