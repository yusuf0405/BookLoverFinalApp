package com.joseph.stories.presentation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.joseph.common_api.base.BaseBindingFragment
import com.joseph.stories.databinding.FragmentMainStoriesBinding
import com.joseph.stories.presentation.adapter.StoriesViewPagerAdapter
import com.joseph.stories.presentation.adapter.StoriesViewPagerTransformer
import com.joseph.stories.presentation.models.UserStoriesModel
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.joseph.utils_core.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainStoriesFragment :
    BaseBindingFragment<FragmentMainStoriesBinding>(FragmentMainStoriesBinding::inflate) {

    private val itemPosition: Int by lazy(LazyThreadSafetyMode.NONE) {
        MainStoriesFragmentArgs.fromBundle(requireArguments()).position
    }

    @Inject
    lateinit var factory: StoriesFragmentViewModel.Factory
    private val viewModel: StoriesFragmentViewModel by viewModelCreator {
        factory.create(storiesId = String())
    }

    private var items = mutableListOf<UserStoriesModel>()

    private val adapter: StoriesViewPagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        StoriesViewPagerAdapter(this@MainStoriesFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isFullScreen = true
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        observeResource()
    }

    private fun setupViewPager() = with(binding()) {
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 1
        viewPager.setPageTransformer(StoriesViewPagerTransformer())
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            stories.observe(::setupData)
        }
    }

    private fun setupData(stories: List<UserStoriesModel>) {
        items.clear()
        items.addAll(stories)
        adapter.addItems(items)
        binding().viewPager.setCurrentItem(itemPosition, false)
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    fun showNextStory() {
        if (binding().viewPager.currentItem >= (items.size).minus(1)) navigateBack()
        binding().viewPager.currentItem++
    }

    fun showPreviousStory() {
        if (binding().viewPager.currentItem == 0) navigateBack()
        binding().viewPager.currentItem--
    }

    fun dismiss() {
        navigateBack()
    }
}
