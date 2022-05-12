package com.example.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.cache.models.BookThatReadDb
import com.example.data.cache.models.BookThatReadPosterDb
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*


@Database(entities = [BookThatReadDb::class], version = 1)
@TypeConverters(BookThatReadDbConverter::class)
abstract class BookThatReadDB : RoomDatabase() {
    abstract fun bookDao(): BooksThatReadDao
}

class BookThatReadDbConverter {

    @TypeConverter
    fun fromList(countryLang: List<Boolean?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Boolean?>?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toList(countryLangString: String?): List<Boolean>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Boolean?>?>() {}.type
        return gson.fromJson<List<Boolean>>(countryLangString, type)
    }

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromPoster(bookPosterDb: BookThatReadPosterDb): String {
        return JSONObject().apply {
            put("name", bookPosterDb.name)
            put("url", bookPosterDb.url)
        }.toString()
    }

    @TypeConverter
    fun toPoster(source: String): BookThatReadPosterDb {
        val json = JSONObject(source)
        return BookThatReadPosterDb(
            name = json.getString("name"),
            url = json.getString("url"),
        )
    }
}