package com.joseph.stories.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.joseph.stories.presentation.StoriesFragment
import com.joseph.stories.presentation.models.UserStoriesModel

class StoriesViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var stories = mutableListOf<UserStoriesModel>()

    override fun getItemCount() = stories.size

    override fun createFragment(position: Int): Fragment {
        return StoriesFragment.create(stories[position])
    }

    fun addItems(list: List<UserStoriesModel>) {
        stories.clear()
        stories.addAll(list)
        notifyDataSetChanged()
    }
}