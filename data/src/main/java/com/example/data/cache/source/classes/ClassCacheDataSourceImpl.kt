package com.example.data.cache.source.classes

import com.example.data.cache.db.ClassDao
import com.example.data.cache.models.ClassCache
import com.example.data.models.ClassData
import com.example.domain.Mapper
import javax.inject.Inject

class ClassCacheDataSourceImpl @Inject constructor(
    private val dao: ClassDao,
    private val classDataToCacheMapper: Mapper<ClassData, ClassCache>,
) : ClassCacheDataSource {

    override fun fetchAllClass() = dao.getAllClass()

    override suspend fun saveClasses(classes: List<ClassData>) {
        classes.forEach { classData -> dao.addNewClass(schoolClass = classDataToCacheMapper.map(classData)) }
    }

    override suspend fun addClass(schoolClass: ClassData) =
        dao.addNewClass(classDataToCacheMapper.map(schoolClass))

    override suspend fun deleteClass(id: String) = dao.deleteByClassId(id = id)

    override suspend fun clearTable() = dao.clearTable()
}