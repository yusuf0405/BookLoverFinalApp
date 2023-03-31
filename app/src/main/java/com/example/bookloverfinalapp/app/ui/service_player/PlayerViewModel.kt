package com.example.bookloverfinalapp.app.ui.service_player

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.AudioBookDomain
import com.example.domain.repository.AudioBooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val audioBooksRepository: AudioBooksRepository,
    private val playbackManager: PlaybackManager,
    private val dispatchersProvider: DispatchersProvider,
    private val audioBookDomainToUiMapper: Mapper<AudioBookDomain, AudioBook>
) : BaseViewModel() {

    val audioBookIdFlow = MutableStateFlow<Pair<String, Boolean>?>(null)

    val audioBookFlow = audioBookIdFlow.flatMapLatest {
        val audioBook = if (it != null) audioBooksRepository
            .fetchAudioBookFromCacheObservable(it.first)
            .map(audioBookDomainToUiMapper::map)
        else flow { emit(AudioBook.unknown()) }
        if (it != null && it.second) _playMediaFlow.tryEmit(audioBook.first())
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

    fun prepare(audioBookId: String) {
        audioBookIdFlow.tryEmit(Pair(audioBookId, false))
    }

    fun updateAudioBookIsPlayingState(
        isPlaying: Boolean,
        audioBookId: String? = audioBookIdFlow.value?.first
    ) {
//        if (audioBookFlow.value?.isPlaying == isPlaying) return
//        val id = audioBookId ?: return
//        viewModelScope.launch(dispatchersProvider.io()) {
//            Log.i(
//                "Josephh",
//                "updateAudioBookIsPlayingState = audioBookId =${id} , isPlaying = ${isPlaying}"
//            )
//            audioBooksRepository.updateAudioBookIsPlayingState(
//                audioBookId = id,
//                isPlaying = isPlaying
//            )
//        }
    }

    fun changePlaybackSpeed(playbackSpeed: Float) {
        playbackManager.playbackSpeed = playbackSpeed
        _playbackSpeedFlow.tryEmit(playbackSpeed)
    }
}