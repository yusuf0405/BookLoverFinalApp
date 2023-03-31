package com.joseph.stories.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.joseph.stories.presentation.StoriesItemFragment
import com.joseph.stories.presentation.models.StoriesModel

class MainStoriesViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var stories = mutableListOf<StoriesModel>()

    override fun getItemCount() = stories.size

    override fun createFragment(position: Int) : Fragment {
        return StoriesItemFragment.create(stories[position])
    }

    fun addItems(list: List<StoriesModel>) {
        stories.clear()
        stories.addAll(list)
        notifyDataSetChanged()
    }
}