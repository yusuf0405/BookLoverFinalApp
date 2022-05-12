package com.example.data.cloud.mappers

import com.example.data.cloud.models.BookPosterCloud
import com.example.data.models.BookThatReadData
import com.example.data.models.BookThatReadPosterData
import java.util.*

interface BookMapper {

    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {
        fun map(
            author: String,
            id: String,
            page: Int,
            publicYear: String,
            title: String,
            chapterCount: Int,
            poster: BookPosterCloud,
            updatedAt: Date,
        ): T
    }

    class Base(
        private val author: String,
        private val id: String,
        private val page: Int,
        private val publicYear: String,
        private val title: String,
        private val chapterCount: Int,
        private val poster: BookPosterCloud,
        private val updatedAt: Date,
    ) : BookMapper {
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(
            author = author,
            id = id,
            page = page,
            publicYear = publicYear,
            title = title,
            chapterCount = chapterCount,
            poster = poster,
            updatedAt = updatedAt)
    }

    class ComplexMapper(private val booksMapper: BookMapper) :
        BookThatReadMapper.Mapper<BookThatReadData> {
        override fun map(
            progress: Int,
            objectId: String,
            createdAt: Date,
            chaptersRead: Int,
            bookId: String,
            studentId: String,
            path: String,
            isReadingPages: List<Boolean>,
        ): BookThatReadData = booksMapper.map(object : Mapper<BookThatReadData> {
            override fun map(
                author: String,
                id: String,
                page: Int,
                publicYear: String,
                title: String,
                chapterCount: Int,
                poster: BookPosterCloud,
                updatedAt: Date,
            ): BookThatReadData = BookThatReadData(
                author = author,
                createdAt = createdAt,
                bookId = bookId,
                objectId = objectId,
                page = page,
                publicYear = publicYear,
                title = title,
                chapterCount = chapterCount,
                chaptersRead = chaptersRead,
                poster = BookThatReadPosterData(name = poster.name, url = poster.url),
                updatedAt = updatedAt,
                book = path,
                progress = progress,
                isReadingPages = isReadingPages,
            )

        })
    }
}