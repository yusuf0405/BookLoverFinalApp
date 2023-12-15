package com.example.bookloverfinalapp.app.models

import com.joseph.ui.core.R
import com.example.data.cache.models.IdResourceString

enum class Collections(val title: IdResourceString) {
    ALL_BOOKS(IdResourceString(R.string.all_books)),
    ALL_AUDIO_BOOKS(IdResourceString(R.string.all_audio_books)),
    SAVED_BOOKS(IdResourceString(R.string.saved_books)),
    GENRES(IdResourceString(R.string.genres)),
    USERS(IdResourceString(R.string.my_students));

    companion object {
        fun fetchAllCollections() = listOf(
            ALL_BOOKS,
            ALL_AUDIO_BOOKS,
            SAVED_BOOKS,
            GENRES,
            USERS,
        )
    }
}