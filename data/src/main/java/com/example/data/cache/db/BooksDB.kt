package com.example.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.cache.models.BookDb
import com.example.data.cache.models.BookPdfDb
import com.example.data.cache.models.BookPosterDb
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*

@Database(entities = [BookDb::class], version = 1)
@TypeConverters(BookDbConverter::class)
abstract class BookDB : RoomDatabase() {
    abstract fun bookDao(): BooksDao
}

class BookDbConverter {
    @TypeConverter
    fun fromList(countryLang: List<String?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toList(countryLangString: String?): List<String>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson<List<String>>(countryLangString, type)
    }
    @TypeConverter
    fun fromPoster(bookPosterDb: BookPosterDb): String {
        return JSONObject().apply {
            put("name", bookPosterDb.name)
            put("url", bookPosterDb.url)
        }.toString()
    }

    @TypeConverter
    fun toPoster(source: String): BookPosterDb {
        val json = JSONObject(source)
        return BookPosterDb(
            name = json.getString("name"),
            url = json.getString("url"),
        )
    }

    @TypeConverter
    fun fromPdf(bookPdfDb: BookPdfDb): String {
        return JSONObject().apply {
            put("name", bookPdfDb.name)
            put("url", bookPdfDb.url)
            put("type", bookPdfDb.type)
        }.toString()
    }

    @TypeConverter
    fun toPdf(source: String): BookPdfDb {
        val json = JSONObject(source)
        return BookPdfDb(
            name = json.getString("name"),
            url = json.getString("url"),
            type = json.getString("type"),
        )
    }

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}