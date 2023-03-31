package com.example.bookloverfinalapp.app.ui.service_player

import kotlinx.coroutines.flow.SharedFlow

interface PlayerCallback {

    val playerStatusFlow: SharedFlow<PlayerStatus>

    fun play(audioBookId: String)

    fun playLatestListeningAudio()

    fun stop()

}