package com.example.bookloverfinalapp.app.ui.service_player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.core.extensions.isServiceRunning
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*


private const val LATEST_LISTENING_AUDIO_KEY = "LATEST_LISTENING_AUDIO"
private const val LATEST_LISTENING_AUDIO_ID_KEY = "LATEST_LISTENING_AUDIO_ID_KEY"

@AndroidEntryPoint
abstract class BasePlayerActivity : AppCompatActivity(), PlayerCallback,
    FragmentPlaybackSpeedDialog.OnPlaybackChangedListener {

    protected abstract val binding: ActivityMainBinding

    protected abstract val playerOverlayContainerConstraintLayout: View

    //    private val playerOverlayPlayMaterialButton: MaterialButton by lazy(
//        LazyThreadSafetyMode.NONE
//    ) {
//        binding.includeBottomSheetPlayer.playerOverlayPlayMaterialButton
//    }
//    private val playbackSpeedButton: TextView by lazy(
//        LazyThreadSafetyMode.NONE
//    ) {
//        binding.includeBottomSheetPlayer.playerView.findViewById(R.id.playbackSpeedButton)
//    }
    private var audioBook: AudioBook? = null

    protected var playerSate = false

    private inner class PlayerWindowCallback(val originalCallback: Window.Callback) :
        BaseWindowCallback(originalCallback) {

        override fun dispatchTouchEvent(event: MotionEvent): Boolean {
//             Collapse the bottom sheet if touch will not be handle by the bottom sheet view.
            if (event.action == MotionEvent.ACTION_UP &&
                !playerOverlayContainerConstraintLayout.pointInView(event.x, event.y)
            ) {
//                audioService?.audioBookId?.let { audioBookId -> viewModel.play(audioBookId = audioBookId) }
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
//            binding.includeBottomSheetPlayer.playerView.player = binder.exoPlayer
            // Pass player updates to interested observers
            // .

//            audioService?.playerStatusFlow?.onEach { status ->
//                _playerStatusFlow.tryEmit(status)
//                playerOverlayPlayMaterialButton.isSelected = status is PlayerStatus.Playing
//                viewModel.updateAudioBookIsPlayingState(status is PlayerStatus.Playing)
//                when (status) {
//                    is PlayerStatus.Playing -> {
//                        Log.i("Joseph", "PlayerStatus = Playing")
//                        startRotateAnimToImageView()
//                        playerOverlayPlayMaterialButton.setIconResource(R.drawable.pause_icon)
//                    }
//                    is PlayerStatus.Paused -> {
//                        Log.i("Joseph", "PlayerStatus = Paused")
//                        cancelRotateAnimToImageView()
//                        playerOverlayPlayMaterialButton.setIconResource(R.drawable.play_icon)
//                    }
//                    is PlayerStatus.Cancelled -> {
//                        Log.i("Joseph", "PlayerStatus = Cancelled")
//                        dismissPlayerOverlay()
//                        stopAudioService()
//                    }
////                    else -> Unit
//                }
//            }?.launchIn(lifecycleScope)

            // Show player after config change.
            val episodeId = audioService?.audioBookId
            if (episodeId != null) {
                showPlayerOverlay()
//                viewModel.refreshIfNecessary(episodeId)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioService = null
        }
    }

    private val _playerStatusFlow = createSharedFlowAsLiveData<PlayerStatus>()
    override val playerStatusFlow: SharedFlow<PlayerStatus> get() = _playerStatusFlow.asSharedFlow()

    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupPlayerBottomSheet()
        setOnClickListeners()
//        observeData()
//        binding.includeBottomSheetPlayer.playerView.showController()
    }

    private fun setOnClickListeners() = with(binding) {
//        playerOverlayPlayMaterialButton.setOnDownEffectClickListener {
//            if (playerOverlayPlayMaterialButton.isSelected) audioService?.pause()
//            else audioService?.resume()
//        }
//
//        playbackSpeedButton.setOnDownEffectClickListener {
//            showChoiceSchoolModalPage((it as TextView).text.toString())
//        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerOverlayContainerConstraintLayout
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.playerOverlayContainerConstraintLayout, FragmentPlayer())
//            .commit()

    }

//    private fun observeData() = with(viewModel) {
//        playbackSpeedFlow.onEach { playbackSpeed ->
//            val formatter = DecimalFormat("0.#")
//            playbackSpeedButton.text =
//                getString(R.string.playback_speed, formatter.format(playbackSpeed))
//            audioService?.changePlaybackSpeed(playbackSpeed)
//            binding.includeBottomSheetPlayer.playerView.showController()
//        }.launchIn(lifecycleScope)
//
//        viewModel.playMediaFlow.onEach { audioBook ->
//            AudioService.newIntent(this@BasePlayerActivity, audioBook).also { intent ->
//                 This service will get converted to foreground service using the PlayerNotificationManager notification Id.
//                startService(intent)
//            }
//        }.launchIn(lifecycleScope)
//
//        audioBookFlow.filter { it != AudioBook.unknown() }
//            .onEach { audioBook = it }
//            .onEach(::renderContent)
//            .launchIn(lifecycleScope)
//        audioBookIdFlow.launchIn(lifecycleScope)
//    }

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

    override fun playLatestListeningAudio() {
        val latestListeningAudioId = latestListeningAudioId()
        if (latestListeningAudioId.isNullOrEmpty()) return
        saveLatestListeningAudioId(latestListeningAudioId)
        showPlayerOverlay()
        bindToAudioService()
//        viewModel.prepare(latestListeningAudioId)
    }

    override fun onStop() {
        unbindAudioService()
        super.onStop()
    }

    override fun onPlaybackSpeedChanged(playbackSpeed: Float) {
//        viewModel.changePlaybackSpeed(playbackSpeed)
    }

    override fun play(audioBookId: String) {
        saveLatestListeningAudioId(audioBookId)
        showPlayerOverlay()
        bindToAudioService()
//        viewModel.play(audioBookId)
    }

    private fun saveLatestListeningAudioId(audioBookId: String) =
        getSharedPreferences(LATEST_LISTENING_AUDIO_KEY, Context.MODE_PRIVATE)
            .edit()
            .putString(LATEST_LISTENING_AUDIO_ID_KEY, audioBookId)
            .apply()

    private fun latestListeningAudioId() =
        getSharedPreferences(LATEST_LISTENING_AUDIO_KEY, Context.MODE_PRIVATE)
            .getString(LATEST_LISTENING_AUDIO_ID_KEY, String())

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

//        binding.includeBottomSheetPlayer.playerOverlayPeekLinearLayout.setOnClickListener {
//            togglePlayerOverlayShowState()
//        }

//        binding.includeBottomSheetPlayer.playerView.hide()
        BottomSheetBehavior.from(playerOverlayContainerConstraintLayout).apply {
            isGestureInsetBottomIgnored = true
//            peekHeight = 41.toDp

            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) = when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Log.i("Joseph", "STATE_HIDDEN")
                        binding.bottomNavigationView.hide()
                        setPlayerOverlayPlaceHolder(false)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        Log.i("Joseph", "STATE_EXPANDED")
                        binding.bottomNavigationView.hide()
                        setPlayerOverlayPeekVisibility(false)
                        setPlayerOverlayPlaceHolder(true)
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.bottomNavigationView.show()
                        setPlayerOverlayPeekVisibility(true)

                        Log.i("Joseph", "STATE_COLLAPSED")
                        setPlayerOverlayPlaceHolder(true)
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        setPlayerOverlayPeekVisibility(false)
                        Log.i("Joseph", "STATE_DRAGGING")
                        Unit
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        Log.i("Joseph", "STATE_SETTLING")
                        Unit
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        Log.i("Joseph", "STATE_HALF_EXPANDED")
                        Unit
                    }

                    else -> Unit
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
            })
        }
    }

//

    private fun createCustomTarget(cardView: MaterialCardView) = object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            createPaletteAsync(resource, cardView)
        }

        override fun onLoadCleared(placeholder: Drawable?) = Unit
    }

    private fun createPaletteAsync(bitmap: Bitmap, cardView: MaterialCardView) {
        Palette.from(bitmap).generate { palette ->
            val color = when {
                palette?.dominantSwatch != null -> palette.dominantSwatch!!.rgb
                palette?.lightVibrantSwatch != null -> palette.lightVibrantSwatch!!.rgb
                palette?.lightMutedSwatch != null -> palette.lightMutedSwatch!!.rgb
                palette?.vibrantSwatch != null -> palette.vibrantSwatch!!.rgb
                palette?.mutedSwatch != null -> palette.mutedSwatch!!.rgb
                palette?.darkVibrantSwatch != null -> palette.darkVibrantSwatch!!.rgb
                else -> getColor(R.color.rating_second_color)
            }
            cardView.setCardBackgroundColor(color)
        }
    }


    private fun cancelRotateAnimToImageView() {
//        binding.includeBottomSheetPlayer.playerOverlayImageView.clearAnimation()
    }

    fun showPlayerOverlay() {
        Log.i("Joseph", "showPlayerOverlay")
        // Monitor outside touches when player overlay is collapsed or expanded only.
        if (audioBook == null) return
        window.callback = PlayerWindowCallback(window.callback)
        BottomSheetBehavior.from(playerOverlayContainerConstraintLayout).apply {
            isHideable = false
            setPlayerOverlayPlaceHolder(isVisible = true)
        }
    }

    private fun collapsePlayerOverlay() {
        Log.i("Joseph", "collapsePlayerOverlay")
        BottomSheetBehavior.from(playerOverlayContainerConstraintLayout).apply {
            setPlayerOverlayPeekVisibility(true)
            if (state == BottomSheetBehavior.STATE_EXPANDED) state =
                BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun togglePlayerOverlayShowState() {
        Log.i("Joseph", "togglePlayerOverlayShowState")
        BottomSheetBehavior.from(playerOverlayContainerConstraintLayout).apply {
            val collapse = state == BottomSheetBehavior.STATE_EXPANDED
            Log.i("Joseph", "togglePlayerOverlayShowState collapse = ${collapse}")
            setPlayerOverlayPeekVisibility(collapse)
            state = if (collapse) BottomSheetBehavior.STATE_COLLAPSED
            else BottomSheetBehavior.STATE_EXPANDED
        }
    }

    fun setPlayerStateToCollapsed() {
        Log.i("Joseph", "setPlayerStateToCollapsed")
        BottomSheetBehavior.from(playerOverlayContainerConstraintLayout).apply {
            isHideable = true
            setPlayerOverlayPlaceHolder(isVisible = false)
            state = BottomSheetBehavior.STATE_HIDDEN
        }
//        binding.includeBottomSheetPlayer.root.hide()
    }

    fun setPlayerStateToExpended() {
//        binding.includeBottomSheetPlayer.root.show()
    }

    private fun setPlayerOverlayPeekVisibility(isVisible: Boolean) {
//        binding.includeBottomSheetPlayer.playerOverlayPeekLinearLayout.isVisible = isVisible
//        binding.includeBottomSheetPlayer.playerView.isVisible = !isVisible
    }

    private fun dismissPlayerOverlay() {
        Log.i("Joseph", "dismissPlayerOverlay")
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
//        binding.includeContentMain.collapsedPlayerOverlayPlaceHolderView.isVisible = isVisible
    }
}