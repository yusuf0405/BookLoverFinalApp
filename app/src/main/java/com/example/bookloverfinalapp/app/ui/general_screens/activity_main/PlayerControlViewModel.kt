package com.example.bookloverfinalapp.app.ui.general_screens.activity_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.ui.service_player.PlayerStatus
import com.example.bookloverfinalapp.app.utils.extensions.createSharedFlowAsLiveData
import com.example.bookloverfinalapp.app.utils.extensions.createSharedFlowAsSingleLiveEvent
import com.google.android.exoplayer2.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val COLLAPSED_OPACITY_PLAYER_VISIBILITY = 1f

@HiltViewModel
class PlayerControlViewModel @Inject constructor() : ViewModel() {

    data class OpacityExpanded(val reducedAlpha: Float, val opacity: Float)

    private val _playerStateFlow = MutableStateFlow(PlayerState.STATE_COLLAPSED)
    val playerStateFlow: StateFlow<PlayerState> = _playerStateFlow.asStateFlow()

    private val _playerStatusFlow = createSharedFlowAsLiveData<PlayerStatus>()
    val playerStatusFlow: SharedFlow<PlayerStatus> get() = _playerStatusFlow.asSharedFlow()

    private val _collapsePlayerOverlay = createSharedFlowAsLiveData<Unit>()
    val collapsePlayerOverlay: SharedFlow<Unit> = _collapsePlayerOverlay.asSharedFlow()

    private val _showOpacityCollapsed = MutableStateFlow(COLLAPSED_OPACITY_PLAYER_VISIBILITY)
    val showOpacityCollapsed: StateFlow<Float> = _showOpacityCollapsed.asStateFlow()

    private val _showOpacityExpanded = MutableStateFlow(OpacityExpanded(0F, 0F))
    val showOpacityExpanded: StateFlow<OpacityExpanded> = _showOpacityExpanded.asStateFlow()

    private val _isInCollapsedStateflow =
        createSharedFlowAsLiveData<Boolean>().apply { tryEmit(true) }
    val isInCollapsedStateFlow get() = _isInCollapsedStateflow.asSharedFlow()

    private val _exoPlayer = createSharedFlowAsSingleLiveEvent<ExoPlayer>()
    val exoPlayer: SharedFlow<ExoPlayer> = _exoPlayer.asSharedFlow()

    private val _startRenderContent = createSharedFlowAsLiveData<AudioBook?>()
    val startRenderContent: SharedFlow<AudioBook?> = _startRenderContent.asSharedFlow()

    private val _playbackSpeedFlow = MutableStateFlow(0f)
    val playbackSpeedFlow: StateFlow<Float> get() = _playbackSpeedFlow.asStateFlow()

    private val _dismissPlayerOverlay = createSharedFlowAsLiveData<Unit>()
    val dismissPlayerOverlay: SharedFlow<Unit> = _dismissPlayerOverlay.asSharedFlow()

    fun setPlaybackSpeed(playbackSpeedFlow: StateFlow<Float>) {
        viewModelScope.launch {
            _playbackSpeedFlow.emitAll(playbackSpeedFlow)
        }
    }


    fun startRenderContent(audioBook: AudioBook?) {
        _startRenderContent.tryEmit(audioBook)
    }

    fun setExoPlayer(exoPlayer: ExoPlayer) {
        _exoPlayer.tryEmit(exoPlayer)
    }

    fun setPlayerStatusFlow(status: PlayerStatus) {
        _playerStatusFlow.tryEmit(status)
    }

    fun setPlayerState(state: PlayerState) {
        _playerStateFlow.tryEmit(state)
    }

    fun dismissPlayerOverlay() = _dismissPlayerOverlay.tryEmit(Unit)

    fun collapsePlayerOverlay() = _collapsePlayerOverlay.tryEmit(Unit)

    fun transitModes(level: Float) {
        var opacityCollapsed = 2 * (1.0F - level) - 1
        if (level < 0) {
            opacityCollapsed = 0f
        }

        var reducedAlpha = 14 * level - 13
        if (reducedAlpha < 0) {
            reducedAlpha = 0f
        }

        var shouldSendOpticalEvent = true
        if (level == 1f) {
            useExpandedPlayerMode()
            shouldSendOpticalEvent = false
        }
        if (level == 0f) {
            useCollapsedPlayerMode()
            shouldSendOpticalEvent = false
        }

        if (!shouldSendOpticalEvent) return
        _showOpacityCollapsed.tryEmit(opacityCollapsed)
        _showOpacityExpanded.tryEmit(OpacityExpanded(reducedAlpha, level))
    }

    fun useExpandedPlayerMode() {
        _isInCollapsedStateflow.tryEmit(false)
    }

    fun useCollapsedPlayerMode() {
        _isInCollapsedStateflow.tryEmit(true)
    }

}