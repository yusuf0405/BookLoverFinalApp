package com.joseph.stories.presentation.models

import com.joseph.ui.core.adapter.Item


data class StoriesAddAdapterItem(
    val listener: StoriesItemOnClickListener
) : Item

data class StoriesAdapterItem(
    val storiesId: String,
    val userId: String,
    val name: String,
    val previewImageUrl: String,
    val stories: List<StoriesModel>,
    val listener: StoriesItemOnClickListener
) : Item


interface StoriesItemOnClickListener {

    fun storiesOnClickListener(position: Int)

    fun addStoriesOnClickListener()

}
