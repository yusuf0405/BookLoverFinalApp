package com.example.bookloverfinalapp.app.ui.general_screens.activity_main

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseBindingFragment
import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.ui.service_player.PlayerStatus
import com.example.bookloverfinalapp.app.utils.extensions.hide
import com.example.bookloverfinalapp.app.utils.extensions.makeTicker
import com.example.bookloverfinalapp.app.utils.extensions.show
import com.example.bookloverfinalapp.databinding.FragmentPlayScreenBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.material.card.MaterialCardView
import com.joseph.ui.core.extensions.launchWhenViewStarted
import com.joseph.core.extensions.showImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import java.text.DecimalFormat


@AndroidEntryPoint
class PlayScreenFragment :
    BaseBindingFragment<FragmentPlayScreenBinding>(FragmentPlayScreenBinding::inflate) {

    private val viewModel: PlayerControlViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isFullScreen = true
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        observeResource()
    }

    private fun setOnClickListeners() = with(binding()) {
        audioPlayerCollapsed.root.setOnClickListener {
            viewModel.useExpandedPlayerMode()
            viewModel.collapsePlayerOverlay()
        }
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            startRenderContent.observe(::renderContent)
            playerStateFlow.observe(::handleBottomSheetState)
            showOpacityExpanded.map { it.opacity }.observe(::visibilityOpacityExpanded)
            showOpacityCollapsed.observe(::visibilityOpacityCollapsed)
            isInCollapsedStateFlow.observe(::handleIsInCollapsedState)
            exoPlayer.observe(::handleExoPlayer)
            playerStatusFlow.observe(::handlePlayerStatus)
            playbackSpeedFlow.observe(::handlePlaybackSpeed)
            dismissPlayerOverlay.observe { hidePlayer() }
        }
    }

    private fun handlePlaybackSpeed(speed: Float) {
        val formatter = DecimalFormat("0.#")
        binding().audioControllerExpended.playbackSpeedButton.text =
            getString(R.string.playback_speed, formatter.format(speed))
    }

    private fun renderContent(audioBook: AudioBook?) = with(binding()) {
        if (audioBook?.title == null) {
            hidePlayer()
            return@with
        }
        if (audioBook == AudioBook.unknown()) {
            hidePlayer()
            return@with
        }
        val posterUrl = audioBook.audioBookPoster.url
        val title = audioBook.title
        with(binding().audioControllerExpended) {
            titleTextView.text = title
            authorTextView.text = audioBook.author
            description.text = audioBook.description
            authorTextView.makeTicker()
            titleTextView.makeTicker()
            requireContext().showImage(posterUrl, posterImageView)
            Glide.with(requireActivity())
                .asBitmap()
                .load(posterUrl)
                .into(createCustomTarget(posterBackgroundImageView))
        }

        with(binding().audioPlayerCollapsed) {
            playerOverlayTitleTextView.text = title
            playerOverlayAuthorTextView.text = audioBook.author
            playerOverlayTitleTextView.makeTicker()
            playerOverlayAuthorTextView.makeTicker()
            requireContext().showImage(posterUrl, playerOverlayImageView)
        }
    }

    private fun createCustomTarget(cardView: MaterialCardView) = object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            createPaletteAsync(resource, cardView)
        }

        override fun onLoadCleared(placeholder: Drawable?) = Unit
    }

    private fun createPaletteAsync(bitmap: Bitmap, cardView: MaterialCardView) {
        Palette.from(bitmap).generate { palette ->
            val color = when {
                palette?.lightVibrantSwatch != null -> palette.lightVibrantSwatch!!.rgb
                palette?.dominantSwatch != null -> palette.dominantSwatch!!.rgb
                palette?.lightMutedSwatch != null -> palette.lightMutedSwatch!!.rgb
                palette?.vibrantSwatch != null -> palette.vibrantSwatch!!.rgb
                palette?.mutedSwatch != null -> palette.mutedSwatch!!.rgb
                palette?.darkVibrantSwatch != null -> palette.darkVibrantSwatch!!.rgb
                else -> requireContext().getColor(R.color.rating_second_color)
            }
            cardView.setCardBackgroundColor(color)
        }
    }


    private fun handlePlayerStatus(status: PlayerStatus) {
        val isSelected = status is PlayerStatus.Playing
        binding().audioPlayerCollapsed.playerOverlayPlayMaterialButton.isSelected = isSelected
        when (status) {
            is PlayerStatus.Playing -> {
                startRotateAnimToImageView()
                setPlayerExpendedButtonImage(R.drawable.pause_icon)
                setPlayerCollapsedButtonImage(R.drawable.pause_icon)
            }

            is PlayerStatus.Paused -> {
                cancelRotateAnimToImageView()
                setPlayerCollapsedButtonImage(R.drawable.play_icon)
                setPlayerExpendedButtonImage(R.drawable.play_icon)
            }

            is PlayerStatus.Cancelled -> {
                viewModel.useCollapsedPlayerMode()
            }

            is PlayerStatus.Ended -> Unit
            is PlayerStatus.Error -> Unit
            is PlayerStatus.Other -> Unit
        }
    }

    private fun setPlayerCollapsedButtonImage(@DrawableRes imageId: Int) {
        binding().audioPlayerCollapsed.playerOverlayPlayMaterialButton.setIconResource(imageId)
    }

    private fun setPlayerExpendedButtonImage(@DrawableRes imageId: Int) {
        binding().audioControllerExpended.exoPause.setImageResource(imageId)
    }

    private fun handleExoPlayer(exoPlayer: ExoPlayer) = with(binding()) {
        audioPlayerCollapsed.playerView.player = exoPlayer
    }

    private fun handleIsInCollapsedState(isCollapsed: Boolean) {
        if (isCollapsed) switchToCollapsed()
        else switchToExpanded()
    }

    private fun hidePlayer() {
//        binding().audioPlayerCollapsed.root.hide()
//        binding().audioControllerExpended.root.hide()
    }

    private fun switchToExpanded() = with(binding()) {
        audioPlayerCollapsed.root.hide()
        audioControllerExpended.root.show()
        audioControllerExpended.root.alpha = 1f
    }


    private fun switchToCollapsed() = with(binding()) {
        audioControllerExpended.root.hide()
        audioPlayerCollapsed.root.show()
        audioPlayerCollapsed.root.alpha = 1f
    }

    private fun visibilityOpacityCollapsed(opacity: Float) {
        binding().audioPlayerCollapsed.root.apply {
            show()
            alpha = opacity
        }
    }

    private fun visibilityOpacityExpanded(opacity: Float) {
        binding().audioControllerExpended.root.apply {
            show()
            alpha = opacity
        }
    }

    private fun handleBottomSheetState(state: PlayerState) = when (state) {
        PlayerState.STATE_EXPANDED -> {
            viewModel.useExpandedPlayerMode()
            setPlayerState(false)
        }

        PlayerState.STATE_COLLAPSED -> {
            viewModel.useCollapsedPlayerMode()
            setPlayerState(true)
        }

        PlayerState.STATE_DRAGGING -> Unit
        PlayerState.STATE_SETTLING -> Unit
        PlayerState.STATE_HIDDEN -> Unit
    }

    private fun setPlayerState(isCollapsed: Boolean) {
        binding().audioPlayerCollapsed.root.isVisible = isCollapsed
        binding().audioControllerExpended.root.isVisible = !isCollapsed
    }

    private fun startRotateAnimToImageView() {
        val rotateAnim = AnimationUtils.loadAnimation(
            requireContext(),
            com.example.bookloverfinalapp.R.anim.rotate_anim
        )
        rotateAnim.interpolator = LinearInterpolator()
        rotateAnim.repeatCount = Animation.INFINITE
        rotateAnim.duration = 3000
        binding().audioPlayerCollapsed.playerOverlayImageView.startAnimation(rotateAnim)
    }

    private fun cancelRotateAnimToImageView() {
        binding().audioPlayerCollapsed.playerOverlayImageView.clearAnimation()
    }

    companion object {
        const val TAG = "PlayScreenFragment"
        private const val IMAGE_ROUNDED_SIZE = 33

        fun newInstance(): PlayScreenFragment {
            val args = Bundle()
            val playScreenFragment = PlayScreenFragment()
            playScreenFragment.arguments = args
            return playScreenFragment
        }
    }
}