package com.example.bookloverfinalapp.app.ui.general_screens.activity_main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.ui.service_player.*
import com.example.bookloverfinalapp.app.utils.extensions.createSharedFlowAsLiveData
import com.example.bookloverfinalapp.app.utils.extensions.pointInView
import com.example.bookloverfinalapp.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.joseph.ui_core.custom.ScrollBottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*


@AndroidEntryPoint
abstract class PlayerControlActivity : AppCompatActivity(), PlayerCallback,
    FragmentPlaybackSpeedDialog.OnPlaybackChangedListener {

    abstract val binding: ActivityMainBinding

    abstract val navController: NavController

    private val viewModel: PlayerControlViewModel by viewModels()

    private val _playerStatusFlow = createSharedFlowAsLiveData<PlayerStatus>()
    override val playerStatusFlow: SharedFlow<PlayerStatus> get() = _playerStatusFlow.asSharedFlow()

    private val bottomSheetBehavior: ScrollBottomSheetBehavior<FrameLayout> by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        BottomSheetBehavior.from(binding.playerContainerView) as ScrollBottomSheetBehavior
    }

    private val playerViewModel: PlayerViewModel by viewModels()

    private var audioService: AudioService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPlayScreenFragment()
        setupPlayer()
        observeData()
    }

    private fun setupPlayScreenFragment() {
        supportFragmentManager.beginTransaction().replace(
            R.id.playerContainerView,
            PlayScreenFragment.newInstance(),
            PlayScreenFragment.TAG
        ).commitAllowingStateLoss()
    }

    private fun setupPlayer() {
        setPlayerToCollapsed()
        bottomSheetBehavior.apply {
            isAllowDragging = true
            isGestureInsetBottomIgnored = true
            addBottomSheetCallback(creteBottomSheetCallback())
        }
    }

    private fun observeData() = with(playerViewModel) {
        viewModel.setPlaybackSpeed(playbackSpeedFlow)

        viewModel.collapsePlayerOverlay.onEach {
            togglePlayerOverlayShowState()
        }.launchIn(lifecycleScope)

        playbackSpeedFlow.onEach { playbackSpeed ->
            audioService?.changePlaybackSpeed(playbackSpeed)
        }.launchIn(lifecycleScope)

        playMediaFlow.onEach { audioBook ->
            AudioService.newIntent(this@PlayerControlActivity, audioBook).also { intent ->
                startService(intent)
            }
        }.launchIn(lifecycleScope)

        audioBookFlow
            .onEach {
                if (it == null) dismissPlayerOverlay()
                if (it == AudioBook.unknown()) dismissPlayerOverlay()
            }
            .onEach(viewModel::startRenderContent)
            .launchIn(lifecycleScope)

        audioBookIdFlow.launchIn(lifecycleScope)

        viewModel.showOpacityCollapsed.onEach {
//            binding.bottomNavigationView.alpha = it
        }.launchIn(lifecycleScope)

    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as AudioService.AudioServiceBinder
            audioService = binder.service
            viewModel.setExoPlayer(binder.exoPlayer)

            audioService?.playerStatusFlow?.onEach { status ->
                _playerStatusFlow.tryEmit(status)
                viewModel.setPlayerStatusFlow(status)
                playerViewModel.updateAudioBookIsPlayingState(status is PlayerStatus.Playing)
                if (status is PlayerStatus.Cancelled) {
                    dismissPlayerOverlay()
                    stopAudioService()
                }
            }?.launchIn(lifecycleScope)

            val episodeId = audioService?.audioBookId ?: return
            showPlayerOverlay()
            playerViewModel.refreshIfNecessary(episodeId)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioService = null
        }
    }


    override fun onStop() {
        unbindAudioService()
        super.onStop()
    }

    override fun onPlaybackSpeedChanged(playbackSpeed: Float) {
        playerViewModel.changePlaybackSpeed(playbackSpeed)
    }

    override fun play(audioBookId: String) {
        showPlayerOverlay()
        bindToAudioService()
        playerViewModel.play(audioBookId)
    }

    override fun playLatestListeningAudio() {

    }

    override fun stop() {
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

    private fun creteBottomSheetCallback() = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            handleBottomSheetState(newState)
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            if (slideOffset <= 0f) return
            viewModel.transitModes(slideOffset)
            if (slideOffset < 0.4) {
                checkAndHideBottomNav()
                return
            }
            if (slideOffset > 0.1) toggle(false)
        }
    }

    private fun checkAndHideBottomNav() {
        val currentDestination = navController.currentDestination?.id ?: return
        val isVisible = when (currentDestination) {
            R.id.fragmentMainScreen -> true
            R.id.fragmentProgress -> true
            R.id.fragment_profile -> true
            else -> false
        }
        if (isVisible) toggle(true)
    }

    private fun toggle(show: Boolean) {
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.duration = if (show) 200 else 800
        transition.addTarget(binding.bottomNavigationView)
        TransitionManager.beginDelayedTransition(binding.root, transition)
        binding.bottomNavigationView.visibility = if (show) View.VISIBLE else View.GONE
    }


    private fun handleBottomSheetState(state: Int) = when (state) {
        BottomSheetBehavior.STATE_EXPANDED -> {
            viewModel.setPlayerState(PlayerState.STATE_EXPANDED)
        }
        BottomSheetBehavior.STATE_COLLAPSED -> {
            Log.i("Joseph", "BottomSheetBehavior = STATE_COLLAPSED")
            viewModel.setPlayerState(PlayerState.STATE_COLLAPSED)
        }
        BottomSheetBehavior.STATE_DRAGGING -> {
            viewModel.setPlayerState(PlayerState.STATE_DRAGGING)
        }
        BottomSheetBehavior.STATE_SETTLING -> {
            viewModel.setPlayerState(PlayerState.STATE_SETTLING)
        }
        BottomSheetBehavior.STATE_HIDDEN -> {
            viewModel.setPlayerState(PlayerState.STATE_HIDDEN)
        }
        else -> Unit
    }


    private fun togglePlayerOverlayShowState() {
        Log.i("Joseph", "togglePlayerOverlayShowState")
        bottomSheetBehavior.apply {
            val collapse = state == BottomSheetBehavior.STATE_EXPANDED
            state = if (collapse) BottomSheetBehavior.STATE_COLLAPSED
            else BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun showPlayerOverlay() {
        Log.i("Joseph", "showPlayerOverlay")
        window.callback = PlayerWindowCallback(window.callback)
        bottomSheetBehavior.isHideable = false
    }


    private fun collapsePlayerOverlay() {
        Log.i("Joseph", "collapsePlayerOverlay")
        bottomSheetBehavior.apply {
            if (state == BottomSheetBehavior.STATE_EXPANDED) state =
                BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun setPlayerToCollapsed() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private inner class PlayerWindowCallback(val originalCallback: Window.Callback) :
        BaseWindowCallback(originalCallback) {

        override fun dispatchTouchEvent(event: MotionEvent): Boolean {
            if (event.action == MotionEvent.ACTION_UP &&
                !binding.playerContainerView.pointInView(event.x, event.y)
            ) {
                audioService?.audioBookId?.let { audioBookId -> playerViewModel.play(audioBookId = audioBookId) }
                collapsePlayerOverlay()
            }
            return super.dispatchTouchEvent(event)
        }
    }

    private fun dismissPlayerOverlay() {
        viewModel.dismissPlayerOverlay()
        (window.callback as? PlayerControlActivity.PlayerWindowCallback)
            ?.originalCallback
            ?.let { window.callback = it }
        bottomSheetBehavior.apply {
            isHideable = true
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

}