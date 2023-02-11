package com.example.bookloverfinalapp.app.ui.player

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.domain.Mapper
import com.example.domain.models.AudioBookDomain
import com.example.domain.repository.AudioBooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val audioBooksRepository: AudioBooksRepository,
    private val playbackManager: PlaybackManager,
    private val audioBookDomainToUiMapper: Mapper<AudioBookDomain, AudioBook>
) : BaseViewModel() {

    val audioBookIdFlow = MutableStateFlow<Pair<String, Boolean>?>(null)

    val audioBookFlow = audioBookIdFlow.map {
        val audioBook = if (it != null) audioBookDomainToUiMapper.map(
            audioBooksRepository.fetchAudioBookFromCache(it.first)
        )
        else AudioBook.unknown()

        if (it != null && it.second) _playMediaFlow.tryEmit(audioBook)
        if (_playbackSpeedFlow.value != playbackManager.playbackSpeed) {
            _playbackSpeedFlow.value = playbackManager.playbackSpeed
        }
        audioBook
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)


    private val _playMediaFlow = createMutableSharedFlowAsSingleLiveEvent<AudioBook>()
    val playMediaFlow: SharedFlow<AudioBook> get() = _playMediaFlow.asSharedFlow()

    private val _playbackSpeedFlow = MutableStateFlow(0f)
    val playbackSpeedFlow: StateFlow<Float> get() = _playbackSpeedFlow.asStateFlow()

    fun refreshIfNecessary(audioBookId: String) {
        if (audioBookFlow.value == null) {
            audioBookIdFlow.tryEmit(Pair(audioBookId, false))
        }
    }

    fun play(audioBookId: String) {
        audioBookIdFlow.tryEmit(Pair(audioBookId, true))
    }

    fun changePlaybackSpeed(playbackSpeed: Float) {
        playbackManager.playbackSpeed = playbackSpeed
        _playbackSpeedFlow.tryEmit(playbackSpeed)
    }

    fun updateAudioBookCurrentStartPosition(audioBookId: String, currentPosition: Int) {
//        launchInBackground {
//            audioBooksRepository.updateAudioBookCurrentStartPosition(
//                audioBookId = audioBookId,
//                currentPosition = currentPosition
//            )
//        }
    }
}