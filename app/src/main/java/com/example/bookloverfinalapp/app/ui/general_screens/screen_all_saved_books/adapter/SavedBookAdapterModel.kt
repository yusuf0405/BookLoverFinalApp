package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.joseph.ui_core.adapter.Item
import com.example.bookloverfinalapp.app.utils.cons.SETTING_LANGUAGE_ENGLISH_KEY
import com.example.bookloverfinalapp.app.utils.cons.SETTING_LANGUAGE_KEY
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

data class SavedBookAdapterModel(
    val savedBook: BookThatRead,
//    val title: String,
//    val author: String,
//    val posterUrl: String,
//    val progress: Int,
//    val pageCount: Int,
    val listeners: SavedBookItemOnClickListeners
) : Item {

    fun id() = savedBook.bookId + savedBook.progress

    fun getCreatedAtText(context: Context): String {
        val language = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(SETTING_LANGUAGE_KEY, SETTING_LANGUAGE_ENGLISH_KEY)
        val prettyTime = PrettyTime(Locale(language ?: SETTING_LANGUAGE_ENGLISH_KEY))
        val getCreatedAt = prettyTime.format(savedBook.createdAt)
        return "${context.getString(R.string.added)}: $getCreatedAt"
    }

    fun getProgressColor(context: Context) =
        when (savedBook.progress.toFloat() / (savedBook.page - 1).toFloat()) {
            in 0f..0.40f -> context.getColor(R.color.widget_icon_background)
            in 0.41f..0.70f -> context.getColor(R.color.books)
            in 0.70f..1f -> context.getColor(R.color.apple_normal)
            else -> context.getColor(R.color.white)
        }
}