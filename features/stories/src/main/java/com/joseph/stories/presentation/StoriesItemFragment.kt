package com.joseph.stories.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.joseph.common.base.BaseBindingFragment
import com.joseph.stories.presentation.StoriesFragment.Companion.DEFAULT_STORIES_SHOW_TIME
import com.joseph.stories.databinding.FragmentStoriesItemBinding
import com.joseph.stories.presentation.gesture.SwipeDetector
import com.joseph.stories.presentation.models.MediaType
import com.joseph.stories.presentation.models.StoriesModel
import com.joseph.core.extensions.setOnDownEffectClickListener
import com.joseph.core.extensions.showBlurImage
import com.joseph.core.extensions.showRoundedImage
import com.joseph.ui.core.extensions.toDp

const val EXTRA_STORY_MODEL = "EXTRA_STORY_MODEL"

class StoriesItemFragment :
    BaseBindingFragment<FragmentStoriesItemBinding>(FragmentStoriesItemBinding::inflate) {

    private var model: StoriesModel? = null

    companion object {
        fun create(model: StoriesModel) =
            StoriesItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_STORY_MODEL, model)
                }
            }
    }

    private val exoPlayer: ExoPlayer by lazy(LazyThreadSafetyMode.NONE) {
        ExoPlayer.Builder(requireContext()).build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isFullScreen = true
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            model = it.getParcelable(EXTRA_STORY_MODEL) as? StoriesModel
        }
    }

    override fun onResume() {
        super.onResume()
        setupData()
        initializePlayer()
        setupClickListeners()
        setupGestures()
    }

    private fun initializePlayer() = with(binding().videoView) {
        player = exoPlayer
        hideController()
        exoPlayer.addListener(PlayerEventListener())
    }

    private fun setupData() {
        updateProgressBarPause(true)
        model?.let(::initializeView)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupGestures() {
        val swipeDetector = GestureDetector(requireContext(), object : SwipeDetector() {
            override fun onSwipe(direction: SwipeDirection): Boolean {
                if (direction == SwipeDirection.DOWN) {
                    val parent = parentFragment as StoriesFragment?
                    parent?.dismiss()
                    return true
                }
                return false
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                val width = binding().clLayout.width
                val pointX = e.x
                val parent = parentFragment as StoriesFragment?
                if (pointX > width / 2f)
                    parent?.showNextStory()
                else
                    parent?.showPreviousStory()
                return super.onSingleTapConfirmed(e)
            }
        })
        val touchListener = View.OnTouchListener { _, event ->
            swipeDetector.onTouchEvent(event)
        }
        binding().clLayout.setOnTouchListener(touchListener)
    }

    private fun initializeView(model: StoriesModel) = with(binding()) {
        tvTitle.text = model.title
        tvDescription.text = model.description
        setViewsForMediaType(mediaType = model.mediaType)
        when (model.mediaType) {
            MediaType.IMAGE -> showStoriesImages(model.imageFileUrl)
            MediaType.VIDEO -> startPlayVideoFromUri(model.videoFileUrl)

        }
        if (isResumed) updateTimePerSegmentMsFlow()
    }

    private fun startPlayVideoFromUri(videoFileUrl: String) = with(binding()) {
        val parent = parentFragment as? StoriesFragment? ?: return
        parent.isProgressBarPause.tryEmit(true)
        handleViewPlayerState(isStart = false)
        play(videoFileUrl)
    }

    private fun play(url: String) {
        exoPlayer.playWhenReady = true
        val mediaSource = ProgressiveMediaSource
            .Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(url))
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
    }

    private fun showStoriesImages(imageFileUrl: String) = with(binding()) {
        updateProgressBarPause(false)
        requireContext().showRoundedImage(roundedSize = 4.toDp, imageFileUrl, imageView)
        requireContext().showBlurImage(blurSize = 25f, imageFileUrl, imageViewBlurBackground)
        progressBar.isVisible = false
    }

    private fun setViewsForMediaType(mediaType: MediaType) = with(binding()) {
        val isVideoFile = mediaType == MediaType.VIDEO
        if (isVideoFile) handleViewPlayerState(isStart = true)
        videoView.isVisible = isVideoFile
        videoViewContainer.isVisible = isVideoFile
        informationContainer.isVisible = true
        imageViewBlurBackground.isVisible = !isVideoFile
        imageView.isVisible = !isVideoFile
    }

    private inner class PlayerEventListener : Player.Listener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState != Player.STATE_READY) return
            updateProgressBarPause(false)
            handlePlaybackStateStateReady()
        }

        override fun onPlayerError(error: PlaybackException) {

        }

        private fun handlePlaybackStateStateReady() {
            if (!exoPlayer.playWhenReady) return
            handleViewPlayerState(isStart = true)
            updateTimePerSegmentMsFlow((exoPlayer.duration))
        }
    }

    private fun handleViewPlayerState(isStart: Boolean) = with(binding()) {
        videoViewContainer.isVisible = isStart
        imageViewBlurBackground.isVisible = !isStart
        progressBar.isVisible = !isStart
        tvTitle.isVisible = isStart
        tvDescription.isVisible = isStart
    }

    private fun updateProgressBarPause(isPause: Boolean) {
        val parent = parentFragment as? StoriesFragment? ?: return
        parent.isProgressBarPause.tryEmit(isPause)
    }

    private fun setupClickListeners() = with(binding()) {
        leftLayout.setOnDownEffectClickListener { showPreviousStory() }
        rightLayout.setOnDownEffectClickListener { showNextStory() }
        ivClose.setOnDownEffectClickListener { dismissStoriesFragment() }
    }

    private fun showPreviousStory() {
        val parent = parentFragment as? StoriesFragment? ?: return
        parent.showPreviousStory()
    }

    private fun updateTimePerSegmentMsFlow(durationInSeconds: Long = DEFAULT_STORIES_SHOW_TIME) {
        val parent = parentFragment as? StoriesFragment? ?: return
        parent.timePerSegmentMsFlow.tryEmit(durationInSeconds)
    }

    private fun showNextStory() {
        val parent = parentFragment as? StoriesFragment? ?: return
        parent.showNextStory()
    }

    private fun dismissStoriesFragment() {
        val parent = parentFragment as? StoriesFragment? ?: return
        parent.dismiss()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.stop()
    }
}