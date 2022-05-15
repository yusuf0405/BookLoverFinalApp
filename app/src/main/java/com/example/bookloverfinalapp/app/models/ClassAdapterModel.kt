package com.example.bookloverfinalapp.app.models

import com.example.bookloverfinalapp.app.base.Abstract

sealed class ClassAdapterModel : Abstract.Object<Unit, ClassAdapterModel.StringMapper>() {

    override fun map(mapper: StringMapper) {}


    object Progress : ClassAdapterModel()

    object Empty : ClassAdapterModel()

    class Base(
        private val id: String,
        private val title: String,
        private val schoolId: String,
    ) : ClassAdapterModel() {
        override fun map(mapper: StringMapper) {
            mapper.map(id = id, title = title, schoolId = schoolId)
        }
    }

    class Fail(private var message: String) : ClassAdapterModel() {
        override fun map(mapper: StringMapper) {
            mapper.map(message)
        }
    }

    interface StringMapper : Abstract.Mapper {
        fun map(text: String)
        fun map(
            id: String,
            title: String,
            schoolId: String,
        )
    }

}