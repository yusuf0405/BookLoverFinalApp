package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.mappers

import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.listener.UserItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.models.UserAdapterModel
import javax.inject.Inject

interface UserDomainToAdapterModelMapper {

    fun map(from: Student, listener: UserItemOnClickListener): UserAdapterModel
}

class UserDomainToAdapterModelMapperImpl @Inject constructor() : UserDomainToAdapterModelMapper {

    override fun map(from: Student, listener: UserItemOnClickListener): UserAdapterModel =
        from.run {
            UserAdapterModel(
                id = objectId,
                name = name,
                lastName = lastname,
                imageUrl = image.url,
                pagesReadCount = progress,
                chaptersReadCount = chaptersRead,
                booksReadCount = booksRead,
                listener = listener
            )
        }
}