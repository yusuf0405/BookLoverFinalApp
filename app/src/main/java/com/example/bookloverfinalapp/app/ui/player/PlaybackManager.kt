package com.example.bookloverfinalapp.app.ui.player

import android.content.SharedPreferences
import javax.inject.Inject

private const val KEY_PLAYBACK_SPEED = "playback_speed"

interface PlaybackManager {

    var playbackSpeed: Float
}

class PlaybackManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : PlaybackManager {
    override var playbackSpeed: Float = sharedPreferences.getFloat(KEY_PLAYBACK_SPEED, 1f)
        set(value) {
            field = value
            val editor = sharedPreferences.edit()
            editor.putFloat(KEY_PLAYBACK_SPEED, value)
            editor.apply()
        }

}