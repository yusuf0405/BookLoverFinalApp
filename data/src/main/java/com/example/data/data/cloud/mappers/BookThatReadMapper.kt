package com.example.data.data.cloud.mappers

import java.util.*

interface BookThatReadMapper {

    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {
        fun map(
            progress: Int,
            objectId: String,
            createdAt: Date,
            chaptersRead: Int,
            bookId: String,
            studentId: String,
            isReadingPages: List<Boolean>,
        ): T
    }

    class Base(
        private val progress: Int,
        private val objectId: String,
        private val createdAt: Date,
        private val chaptersRead: Int,
        private val bookId: String,
        private val studentId: String,
        private val isReadingPages: List<Boolean>,
    ) : BookThatReadMapper {
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(progress,
            objectId = objectId,
            createdAt = createdAt,
            chaptersRead = chaptersRead,
            bookId = bookId,
            studentId = studentId,
            isReadingPages = isReadingPages)
    }
}
