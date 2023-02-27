package com.example.bookloverfinalapp.app.ui.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.snackbar.GenericSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import java.text.DecimalFormat

@AndroidEntryPoint
abstract class BasePlayerActivity : AppCompatActivity(), PlayerCallback,
    FragmentPlaybackSpeedDialog.OnPlaybackChangedListener {

    protected abstract val binding: ActivityMainBinding

    private val playerOverlayContainerConstraintLayout: ConstraintLayout by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        binding.includeBottomSheetPlayer.playerOverlayContainerConstraintLayout
    }

    private val playerOverlayPlayMaterialButton: MaterialButton by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        binding.includeBottomSheetPlayer.playerOverlayPlayMaterialButton
    }
    private val playbackSpeedButton: TextView by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        binding.includeBottomSheetPlayer.playerView.findViewById(R.id.playbackSpeedButton)
    }

    private inner class PlayerWindowCallback(val originalCallback: Window.Callback) :
        BaseWindowCallback(originalCallback) {

        override fun dispatchTouchEvent(event: MotionEvent): Boolean {
//             Collapse the bottom sheet if touch will not be handle by the bottom sheet view.
            if (event.action == MotionEvent.ACTION_UP &&
//                    findViewById<View>(R.id.stopButton)?.pointInView(event.x, event.y) == false &&
                !playerOverlayContainerConstraintLayout.pointInView(event.x, event.y)
            ) {
                audioService?.audioBookId?.let { audioBookId ->
                    viewModel.play(audioBookId = audioBookId)
                }
                collapsePlayerOverlay()
            }
            return super.dispatchTouchEvent(event)
        }

    }

    private var audioService: AudioService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as AudioService.AudioServiceBinder
            audioService = binder.service
            // Attach the ExoPlayer to the PlayerView.
            binding.includeBottomSheetPlayer.playerView.player = binder.exoPlayer
            // Pass player updates to interested observers
            // .

            audioService?.playerStatusFlow?.onEach { status ->
                _playerStatusFlow.tryEmit(status)
                playerOverlayPlayMaterialButton.isSelected = status is PlayerStatus.Playing
                when (status) {
                    is PlayerStatus.Playing -> playerOverlayPlayMaterialButton.setIconResource(R.drawable.pause_icon)
                    is PlayerStatus.Paused -> playerOverlayPlayMaterialButton.setIconResource(R.drawable.play_icon)
                    is PlayerStatus.Cancelled -> {
                        dismissPlayerOverlay()
                        stopAudioService()
                    }
                    else -> Unit
                }
            }?.launchIn(lifecycleScope)

            // Show player after config change.
            val episodeId = audioService?.audioBookId
            if (episodeId != null) {
                showPlayerOverlay()
                viewModel.refreshIfNecessary(episodeId)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioService = null
        }
    }

    private val _playerStatusFlow = createSharedFlowAsLiveData<PlayerStatus>()
    override val playerStatusFlow: SharedFlow<PlayerStatus> get() = _playerStatusFlow.asSharedFlow()

    private val viewModel: PlayerViewModel by viewModels()

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupPlayerBottomSheet()
        setOnClickListeners()
        observeData()
        binding.includeBottomSheetPlayer.playerView.showController()
    }

    private fun setOnClickListeners() = with(binding) {
        playerOverlayPlayMaterialButton.setOnDownEffectClickListener {
            if (playerOverlayPlayMaterialButton.isSelected) audioService?.pause()
            else audioService?.resume()
        }

        playbackSpeedButton.setOnDownEffectClickListener {
            showChoiceSchoolModalPage((it as TextView).text.toString())
        }

    }

    private fun observeData() = with(viewModel) {
        playbackSpeedFlow.onEach { playbackSpeed ->
            val formatter = DecimalFormat("0.#")
            playbackSpeedButton.text =
                getString(R.string.playback_speed, formatter.format(playbackSpeed))
            audioService?.changePlaybackSpeed(playbackSpeed)
        }.launchIn(lifecycleScope)

        viewModel.playMediaFlow.onEach { audioBook ->
            AudioService.newIntent(this@BasePlayerActivity, audioBook).also { intent ->
                // This service will get converted to foreground service using the PlayerNotificationManager notification Id.
                startService(intent)
            }
        }.launchIn(lifecycleScope)

        audioBookFlow.filter { it != AudioBook.unknown() }
            .onEach(::renderContent)
            .launchIn(lifecycleScope)
        audioBookIdFlow.launchIn(lifecycleScope)
    }

    private fun showChoiceSchoolModalPage(currentSpeed: String) = ModalPage.Builder()
        .fragment(FragmentPlaybackSpeedDialog.newInstance(currentSpeed))
        .title(getString(R.string.change_the_playback_speed))
        .build()
        .show(supportFragmentManager, ModalPage.TAG)

    override fun onStart() {
        super.onStart()
        // Show the player, if the audio service is already running.
        if (applicationContext.isServiceRunning(AudioService::class.java.name)) bindToAudioService()
        else dismissPlayerOverlay()
    }

    override fun onStop() {
        unbindAudioService()
        super.onStop()
    }

    override fun onPlaybackSpeedChanged(playbackSpeed: Float) {
        viewModel.changePlaybackSpeed(playbackSpeed)
    }

    override fun play(audioBookId: String) {
        showPlayerOverlay()
        bindToAudioService()
        viewModel.play(audioBookId)
    }

    override fun stop() {
        dismissPlayerOverlay()
        audioService?.audioBookId?.let { episodeId ->
            _playerStatusFlow.tryEmit(PlayerStatus.Paused(episodeId))
        } ?: run {
            _playerStatusFlow.tryEmit(PlayerStatus.Other())
        }
        stopAudioService()
    }

    private fun bindToAudioService() {
        if (audioService != null) return
        AudioService.newIntent(this).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindAudioService() {
        if (audioService == null) return
        unbindService(connection)
        audioService = null
    }

    private fun stopAudioService() {
        audioService?.pause()
        unbindAudioService()
        stopService(Intent(this, AudioService::class.java))
        audioService = null
    }

    private fun setupPlayerBottomSheet() {
        dismissPlayerOverlay()

        binding.includeBottomSheetPlayer.playerOverlayPeekLinearLayout.setOnClickListener {
            togglePlayerOverlayShowState()
        }

        BottomSheetBehavior.from(playerOverlayContainerConstraintLayout).apply {
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) = when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> setPlayerOverlayPlaceHolder(false)
                    BottomSheetBehavior.STATE_EXPANDED -> setPlayerOverlayPlaceHolder(true)
                    BottomSheetBehavior.STATE_COLLAPSED -> setPlayerOverlayPlaceHolder(true)
                    else -> {}
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
            })
        }
    }

    private fun renderContent(audioBook: AudioBook?) = with(binding.includeBottomSheetPlayer) {
        if (audioBook == null) return@with
        val title = audioBook.title
        val titleTextView = playerView.findViewById<TextView>(R.id.titleTextView)
        val authorTextView = playerView.findViewById<TextView>(R.id.authorTextView)
        playerOverlayTitleTextView.text = title
        authorTextView.text = audioBook.author
        titleTextView.text = title
        playerOverlayTitleTextView.makeTicker()
        authorTextView.makeTicker()
        titleTextView.makeTicker()

        Glide.with(this@BasePlayerActivity)
            .load(audioBook.audioBookPoster.url)
            .transform(MultiTransformation(CenterCrop(), CircleCrop()))
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .into(playerOverlayImageView)
    }

    private fun showPlayerOverlay() {
        // Monitor outside touches when player overlay is collapsed or expanded only.
        window.callback = PlayerWindowCallback(window.callback)
        BottomSheetBehavior.from(playerOverlayContainerConstraintLayout).apply {
            isHideable = false
            setPlayerOverlayPlaceHolder(isVisible = true)
        }
    }

    private fun collapsePlayerOverlay() {
        BottomSheetBehavior.from(playerOverlayContainerConstraintLayout).apply {
            if (state == BottomSheetBehavior.STATE_EXPANDED) state =
                BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun togglePlayerOverlayShowState() {
        BottomSheetBehavior.from(playerOverlayContainerConstraintLayout).apply {
            val collapse = state == BottomSheetBehavior.STATE_EXPANDED
            state = if (collapse) BottomSheetBehavior.STATE_COLLAPSED
            else BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun dismissPlayerOverlay() {
        // Restore original window callback.
        (window.callback as? PlayerWindowCallback)?.originalCallback?.let {
            window.callback = it
        }

        BottomSheetBehavior.from(playerOverlayContainerConstraintLayout).apply {
            isHideable = true
            setPlayerOverlayPlaceHolder(isVisible = false)
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun setPlayerOverlayPlaceHolder(isVisible: Boolean) {
        binding.includeContentMain.collapsedPlayerOverlayPlaceHolderView.isVisible = isVisible
    }
}