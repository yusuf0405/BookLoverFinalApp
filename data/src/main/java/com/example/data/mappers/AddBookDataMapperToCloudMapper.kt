package com.example.data.mappers

import com.example.data.cloud.models.AddNewBookCloud
import com.example.data.cloud.models.BookPdfCloud
import com.example.data.cloud.models.BookPosterCloud
import com.example.data.models.AddNewBookData
import com.example.domain.Mapper

class AddBookDataMapperToCloudMapper : Mapper<AddNewBookData, AddNewBookCloud> {
    override fun map(from: AddNewBookData): AddNewBookCloud = from.run {
        AddNewBookCloud(
            title = title,
            publicYear = publicYear,
            page = page,
            author = author,
            description = description,
            poster = BookPosterCloud(name = poster.name, url = poster.url, type = "File"),
            book = BookPdfCloud(name = book.name, type = book.type, url = book.url),
            chapterCount = chapterCount,
            schoolId = schoolId,
            genres = genres,
            isExclusive = isExclusive
        )
    }
}