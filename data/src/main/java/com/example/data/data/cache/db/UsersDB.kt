package com.example.data.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.data.cache.models.StudentDb
import com.example.data.data.cache.models.StudentImageDb
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.util.*

@Database(entities = [StudentDb::class], version = 1)
@TypeConverters(UsersDbConverter::class)
abstract class UsersDB : RoomDatabase() {
    abstract fun usersDao(): UsersDao
}

class UsersDbConverter {
    @TypeConverter
    fun fromPoster(bookPosterDb: StudentImageDb): String {
        return JSONObject().apply {
            put("name", bookPosterDb.name)
            put("url", bookPosterDb.url)
            put("type", bookPosterDb.type)
        }.toString()
    }

    @TypeConverter
    fun toPoster(source: String): StudentImageDb {
        val json = JSONObject(source)
        return StudentImageDb(
            name = json.getString("name"),
            url = json.getString("url"),
            type = json.getString("type")
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
    @TypeConverter
    fun listToJsonString(value: List<String>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()

}