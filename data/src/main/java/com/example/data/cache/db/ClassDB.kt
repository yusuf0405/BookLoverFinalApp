package com.example.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.cache.models.ClassCache


@Database(entities = [ClassCache::class], version = 1)
abstract class ClassDB : RoomDatabase() {
    abstract fun classDao(): ClassDao
}