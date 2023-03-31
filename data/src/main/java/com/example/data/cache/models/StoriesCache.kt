package com.example.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

const val STORIES_TABLE_NAME = "stories_table"

@Entity(tableName = STORIES_TABLE_NAME)
data class StoriesCache(
    @PrimaryKey
    @ColumnInfo(name = "stories_id") val storiesId: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "school_id") val schoolId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_file_url") val imageFileUrl: String,
    @ColumnInfo(name = "preview_image_url") var previewImageUrl: String,
    @ColumnInfo(name = "video_file_url") var videoFileUrl: String,
    @ColumnInfo(name = "is_video_file") val isVideoFile: Boolean,
    @ColumnInfo(name = "published_date") val publishedDate: Date,
)