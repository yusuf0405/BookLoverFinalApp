package com.example.bookloverfinalapp.app.models

import com.example.bookloverfinalapp.app.base.Abstract
import java.util.*

sealed class BookAdapterModel : Abstract.Object<Unit, BookAdapterModel.StringMapper>() {

    override fun map(mapper: StringMapper) {}

    object Progress : BookAdapterModel()

    object Empty : BookAdapterModel()

    class Base(
        var author: String,
        var createdAt: Date,
        var id: String,
        var page: Int,
        var publicYear: String,
        var book: BookPdfAdapter,
        var title: String,
        var chapterCount: Int,
        var poster: BookPosterAdapter,
        var updatedAt: Date,
    ) : BookAdapterModel() {
        override fun map(mapper: StringMapper) {
            mapper.map(author = author,
                createdAt = createdAt,
                id = id,
                page = page,
                publicYear = publicYear,
                book = BookPdf(name = book.name, url = book.url),
                title = title,
                chapterCount = chapterCount,
                poster = BookPoster(name = poster.name, url = poster.url),
                updatedAt = updatedAt)
        }
    }

    class Fail(private var message: String) : BookAdapterModel() {
        override fun map(mapper: StringMapper) {
            mapper.map(message)
        }
    }

    // TODO: improve later
    interface StringMapper : Abstract.Mapper {
        fun map(text: String)
        fun map(
            author: String,
            createdAt: Date,
            id: String,
            page: Int,
            publicYear: String,
            book: BookPdf,
            title: String,
            chapterCount: Int,
            poster: BookPoster,
            updatedAt: Date,
        )
    }

}

data class BookPosterAdapter(
    var name: String,
    var url: String,
)

data class BookPdfAdapter(
    var name: String,
    var type: String,
    var url: String,
)
