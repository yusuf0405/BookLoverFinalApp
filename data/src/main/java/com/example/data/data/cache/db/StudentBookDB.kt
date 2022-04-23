package com.example.data.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.data.cache.models.StudentBookDb
import com.example.data.data.cache.models.StudentBookPdfDb
import com.example.data.data.cache.models.StudentBookPosterDb
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*


@Database(entities = [StudentBookDb::class], version = 1)
@TypeConverters(StudentBookDbConverter::class)
abstract class StudentBookDB : RoomDatabase() {
    abstract fun bookDao(): StudentBooksDao
}

class StudentBookDbConverter {

    @TypeConverter
    fun fromCountryLangList(countryLang: List<Boolean?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Boolean?>?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toCountryLangList(countryLangString: String?): List<Boolean>? {
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
    fun fromPoster(bookPosterDb: StudentBookPosterDb): String {
        return JSONObject().apply {
            put("name", bookPosterDb.name)
            put("url", bookPosterDb.url)
        }.toString()
    }

    @TypeConverter
    fun toPoster(source: String): StudentBookPosterDb {
        val json = JSONObject(source)
        return StudentBookPosterDb(
            name = json.getString("name"),
            url = json.getString("url"),
        )
    }

    @TypeConverter
    fun fromPdf(bookPdfDb: StudentBookPdfDb): String {
        return JSONObject().apply {
            put("name", bookPdfDb.name)
            put("url", bookPdfDb.url)
        }.toString()
    }

    @TypeConverter
    fun toPdf(source: String): StudentBookPdfDb {
        val json = JSONObject(source)
        return StudentBookPdfDb(
            name = json.getString("name"),
            url = json.getString("url"),
        )
    }
}