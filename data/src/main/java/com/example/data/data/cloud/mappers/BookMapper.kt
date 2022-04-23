package com.example.data.data.cloud.mappers

import com.example.data.data.cloud.models.BookPdfCloud
import com.example.data.data.cloud.models.BookPosterCloud
import com.example.data.data.models.StudentBookData
import com.example.data.data.models.StudentBookPdfData
import com.example.data.data.models.StudentBookPosterData
import java.util.*

interface BookMapper {

    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {
        fun map(
            author: String,
            id: String,
            page: Int,
            publicYear: String,
            book: BookPdfCloud,
            title: String,
            chapterCount: Int,
            poster: BookPosterCloud,
            updatedAt: String,
        ): T
    }

    class Base(
        private val author: String,
        private val id: String,
        private val page: Int,
        private val publicYear: String,
        private val book: BookPdfCloud,
        private val title: String,
        private val chapterCount: Int,
        private val poster: BookPosterCloud,
        private val updatedAt: String,
    ) : BookMapper {
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(
            author,
            id,
            page,
            publicYear,
            book,
            title,
            chapterCount,
            poster,
            updatedAt)
    }

    class ComplexMapper(private val booksMapper: BookMapper) :
        BookThatReadMapper.Mapper<StudentBookData> {
        override fun map(
            progress: Int,
            objectId: String,
            createdAt: Date,
            chaptersRead: Int,
            bookId: String,
            studentId: String,
            isReadingPages: List<Boolean>,
        ): StudentBookData = booksMapper.map(object : Mapper<StudentBookData> {
            override fun map(
                author: String,
                id: String,
                page: Int,
                publicYear: String,
                book: BookPdfCloud,
                title: String,
                chapterCount: Int,
                poster: BookPosterCloud,
                updatedAt: String,
            ): StudentBookData = StudentBookData(
                author = author,
                createdAt = createdAt,
                bookId = bookId,
                objectId = objectId,
                page = page,
                publicYear = publicYear,
                title = title,
                chapterCount = chapterCount,
                chaptersRead = chaptersRead,
                poster = StudentBookPosterData(name = poster.name, url = poster.url),
                updatedAt = updatedAt,
                book = StudentBookPdfData(name = book.name, url = book.url),
                progress = progress,
                isReadingPages = isReadingPages,
            )

        })
    }
}