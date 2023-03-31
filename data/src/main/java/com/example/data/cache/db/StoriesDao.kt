package com.example.data.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.cache.models.StoriesCache
import kotlinx.coroutines.flow.Flow

@Dao
interface StoriesDao {

    @Query("select * from STORIES_TABLE")
    fun fetchStoriesObservable(): Flow<MutableList<StoriesCache>>

    @Query("select * from STORIES_TABLE")
    suspend fun fetchStoriesSingle(): MutableList<StoriesCache>

    @Query("select * from STORIES_TABLE where stories_id == :storiesId")
    suspend fun fetchStoriesFromId(storiesId: String): StoriesCache

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewStories(stories: StoriesCache)

    @Query("DELETE FROM STORIES_TABLE")
    fun clearTable()

}