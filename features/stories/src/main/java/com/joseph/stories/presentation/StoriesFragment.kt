package com.joseph.stories.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.joseph.stories.presentation.adapter.MainStoriesViewPagerAdapter
import com.joseph.stories.presentation.models.StoriesModel
import com.joseph.stories.presentation.models.UserStoriesModel
import com.joseph.common.base.BaseBindingFragment
import com.joseph.stories.databinding.FragmentStoriesBinding
import com.joseph.ui.core.custom.progress_bar.SegmentedProgressBarListener
import com.joseph.ui.core.extensions.launchOnViewLifecycle
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class StoriesFragment :
    BaseBindingFragment<FragmentStoriesBinding>(FragmentStoriesBinding::inflate) {

    private var stories = mutableListOf<StoriesModel>()

    private val adapter: MainStoriesViewPagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MainStoriesViewPagerAdapter(this)
    }

    var timePerSegmentMsFlow = MutableStateFlow(DEFAULT_STORIES_SHOW_TIME)
    var isProgressBarPause = MutableSharedFlow<Boolean>(1, 0, BufferOverflow.DROP_OLDEST)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isFullScreen = true
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        setupViewPager()
        observeResource()
        adapter.addItems(stories)
    }

    private fun setupViewPager() = with(binding()) {
        viewPager.adapter = adapter
        progress.viewPager = viewPager
        viewPager.offscreenPageLimit = 1
        setSegmentCount()

        binding().progress.listener = object : SegmentedProgressBarListener {
            override fun onPage(oldPageIndex: Int, newPageIndex: Int) = Unit
            override fun onFinished() {
                showNextStory()
            }
        }
    }


    private fun observeResource() {
        launchOnViewLifecycle {
            timePerSegmentMsFlow.observe(::setTimePerSegmentMs)
        }
        isProgressBarPause.onEach(::setProgressBasState).launchIn(lifecycleScope)

    }

    private fun setProgressBasState(isPause: Boolean) {
        if (isPause) binding().progress.pause()
        else binding().progress.start()

    }

    private fun setTimePerSegmentMs(timePerSegmentMs: Long) {
        binding().progress.timePerSegmentMs = timePerSegmentMs
    }

    private fun setSegmentCount(count: Int = stories.size) {
        binding().progress.segmentCount = count
    }

    fun showNextStory() {
        val parent = parentFragment as? MainStoriesFragment? ?: return
        if (binding().viewPager.currentItem >= (stories.size).minus(1)) parent.showNextStory()
        binding().viewPager.currentItem++
    }

    fun showPreviousStory() {
        val parent = parentFragment as? MainStoriesFragment? ?: return
        if (binding().viewPager.currentItem == 0) parent.showPreviousStory()
        binding().viewPager.currentItem--
    }

    fun dismiss() {
        navigateBack()
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    companion object {
        const val DEFAULT_STORIES_SHOW_TIME = 6000L
        fun create(userStories: UserStoriesModel) =
            StoriesFragment().apply {
                this.stories = userStories.stories.toMutableList()
            }
    }

}