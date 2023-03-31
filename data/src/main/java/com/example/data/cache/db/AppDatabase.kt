package com.example.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.cache.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*

@Database(
    entities = [
        BookCache::class,
        AudioBookCache::class,
        BookThatReadCache::class,
        TaskCache::class,
        ClassCache::class,
        UserStatisticCache::class,
        GenreCache::class,
        StoriesCache::class,
        UserCache::class],
    version = 11,
    exportSchema = true,
)

@TypeConverters(AppDatabase.DatabaseConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getBooksDao(): BooksDao

    abstract fun getBooksThatReadDao(): BooksThatReadDao

    abstract fun getClassesDao(): ClassDao

    abstract fun getUsersDao(): UsersDao

    abstract fun audioBooksDao(): AudioBooksDao

    abstract fun tasksDao(): TasksDao
    abstract fun userStatisticDao(): UserStatisticDao

    abstract fun genresDao(): GenreDao

    abstract fun storiesDao(): StoriesDao

    class DatabaseConverter {

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
        fun toDate(dateLong: Long?): Date? {
            return dateLong?.let { Date(it) }
        }

        @TypeConverter
        fun fromDate(date: Date?): Long? {
            return date?.time
        }

        @TypeConverter
        fun fromBooleanList(countryLang: List<Boolean?>?): String? {
            if (countryLang == null) {
                return null
            }
            val gson = Gson()
            val type: Type = object : TypeToken<List<Boolean?>?>() {}.type
            return gson.toJson(countryLang, type)
        }

        @TypeConverter
        fun toBooleanList(countryLangString: String?): List<Boolean>? {
            if (countryLangString == null) {
                return null
            }
            val gson = Gson()
            val type: Type = object : TypeToken<List<Boolean?>?>() {}.type
            return gson.fromJson<List<Boolean>>(countryLangString, type)
        }


        @TypeConverter
        fun fromBookThatReadPosterCache(bookThatReadPosterCache: BookThatReadPosterCache): String {
            return JSONObject().apply {
                put("bookThatReadPosterCache.name", bookThatReadPosterCache.name)
                put("bookThatReadPosterCache.url", bookThatReadPosterCache.url)
            }.toString()
        }

        @TypeConverter
        fun toBookThatReadPosterCache(source: String): BookThatReadPosterCache {
            val json = JSONObject(source)
            return BookThatReadPosterCache(
                name = json.getString("bookThatReadPosterCache.name"),
                url = json.getString("bookThatReadPosterCache.url"),
            )
        }

        @TypeConverter
        fun fromUserImageCache(userImageCache: UserImageCache): String {
            return JSONObject().apply {
                put("userImageCache.name", userImageCache.name)
                put("userImageCache.url", userImageCache.url)
                put("userImageCache.type", userImageCache.type)
            }.toString()
        }

        @TypeConverter
        fun toUserImageCache(source: String): UserImageCache {
            val json = JSONObject(source)
            return UserImageCache(
                name = json.getString("userImageCache.name"),
                url = json.getString("userImageCache.url"),
                type = json.getString("userImageCache.type")
            )
        }

        @TypeConverter
        fun fromPoster(bookPosterCache: BookPosterCache): String {
            return JSONObject().apply {
                put("bookPosterCache.name", bookPosterCache.name)
                put("bookPosterCache.url", bookPosterCache.url)
            }.toString()
        }

        @TypeConverter
        fun toPoster(source: String): BookPosterCache {
            val json = JSONObject(source)
            return BookPosterCache(
                name = json.getString("bookPosterCache.name"),
                url = json.getString("bookPosterCache.url"),
            )
        }

        @TypeConverter
        fun fromPdf(bookPdfCache: BookPdfCache): String {
            return JSONObject().apply {
                put("bookPdfCache.name", bookPdfCache.name)
                put("bookPdfCache.url", bookPdfCache.url)
                put("bookPdfCache.type", bookPdfCache.type)
            }.toString()
        }

        @TypeConverter
        fun toPdf(source: String): BookPdfCache {
            val json = JSONObject(source)
            return BookPdfCache(
                name = json.getString("bookPdfCache.name"),
                url = json.getString("bookPdfCache.url"),
                type = json.getString("bookPdfCache.type"),
            )
        }

        @TypeConverter
        fun fromAudioBookFile(audioBookFileCache: AudioBookFileCache): String {
            return JSONObject().apply {
                put(AUDIO_BOOK_FILE_NAME_KEY, audioBookFileCache.name)
                put(AUDIO_BOOK_FILE_URL_KEY, audioBookFileCache.url)
            }.toString()
        }

        @TypeConverter
        fun toAudioBookFile(source: String): AudioBookFileCache {
            val json = JSONObject(source)
            return AudioBookFileCache(
                name = json.getString(AUDIO_BOOK_FILE_NAME_KEY),
                url = json.getString(AUDIO_BOOK_FILE_URL_KEY),
            )
        }

        @TypeConverter
        fun fromAudioBookPoster(audioBookFileCache: AudioBookPosterCache): String {
            return JSONObject().apply {
                put(AUDIO_BOOK_POSTER_NAME_KEY, audioBookFileCache.name)
                put(AUDIO_BOOK_POSTER_URL_KEY, audioBookFileCache.url)
            }.toString()
        }

        @TypeConverter
        fun toAudioBookPoster(source: String): AudioBookPosterCache {
            val json = JSONObject(source)
            return AudioBookPosterCache(
                name = json.getString(AUDIO_BOOK_POSTER_NAME_KEY),
                url = json.getString(AUDIO_BOOK_POSTER_URL_KEY),
            )
        }

        @TypeConverter
        fun fromGenrePoster(genreCache: GenrePosterCache): String {
            return JSONObject().apply {
                put(GENRE_POSTER_NAME_KEY, genreCache.name)
                put(GENRE_POSTER_URL_KEY, genreCache.url)
            }.toString()
        }

        @TypeConverter
        fun toGenrePoster(source: String): GenrePosterCache {
            val json = JSONObject(source)
            return GenrePosterCache(
                name = json.getString(GENRE_POSTER_NAME_KEY),
                url = json.getString(GENRE_POSTER_URL_KEY),
            )
        }
    }

    private companion object {
        const val AUDIO_BOOK_FILE_NAME_KEY = "AUDIO_BOOK_FILE_NAME_KEY"
        const val AUDIO_BOOK_POSTER_NAME_KEY = "AUDIO_BOOK_POSTER_NAME_KEY"
        const val AUDIO_BOOK_FILE_URL_KEY = "AUDIO_BOOK_FILE_URL_KEY"
        const val AUDIO_BOOK_POSTER_URL_KEY = "AUDIO_BOOK_POSTER_URL_KEY"
        const val GENRE_POSTER_URL_KEY = "GENRE_POSTER_URL_KEY"
        const val GENRE_POSTER_NAME_KEY = "GENRE_POSTER_NAME_KEY"
    }
}

