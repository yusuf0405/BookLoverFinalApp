package com.example.bookloverfinalapp.app.ui.player

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.ui.general_screens.activity_main.ActivityMain
import com.example.bookloverfinalapp.app.utils.extensions.createSharedFlowAsLiveData
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.BuildConfig
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

private const val PLAYBACK_CHANNEL_ID = "playback_channel"
private const val PLAYBACK_NOTIFICATION_ID = 1
private const val MEDIA_SESSION_TAG = "sed_audio"

private const val PLAYBACK_TIMER_DELAY = 5 * 1000L

private const val ARG_EPISODE_ID = "episode_id"
private const val ARG_URI = "uri_string"
private const val ARG_TITLE = "title"
private const val ARG_START_POSITION = "start_position"

@AndroidEntryPoint
class AudioService : LifecycleService() {

    inner class AudioServiceBinder : Binder() {
        val service
            get() = this@AudioService

        val exoPlayer
            get() = this@AudioService.exoPlayer
    }

    companion object {

        @MainThread
        fun newIntent(context: Context, audioBook: AudioBook? = null) =
            Intent(context, AudioService::class.java).apply {
                audioBook?.let {
                    putExtra(ARG_EPISODE_ID, audioBook.id)
                    putExtra(ARG_TITLE, audioBook.title)
                    putExtra(ARG_URI, Uri.parse(audioBook.audioBookFile.url))
                    putExtra(ARG_START_POSITION, audioBook.currentStartPosition)
                }
            }

    }

    @Inject
    lateinit var playbackManager: PlaybackManager

    private var playbackTimer: Timer? = null

    var audioBookId: String? = null
        private set

    private var audioBookTitle: String? = null

    private lateinit var exoPlayer: SimpleExoPlayer

    private var playerNotificationManager: PlayerNotificationManager? = null
    private var mediaSession: MediaSessionCompat? = null
    private var mediaSessionConnector: MediaSessionConnector? = null

    private val _playerStatusFlow = createSharedFlowAsLiveData<PlayerStatus>()
    val playerStatusFlow: SharedFlow<PlayerStatus> get() = _playerStatusFlow.asSharedFlow()

    private val _updateAudioBookPosition = createSharedFlowAsLiveData<Pair<String, Int>>()
    val updateAudioBookPosition: SharedFlow<Pair<String, Int>> get() = _updateAudioBookPosition.asSharedFlow()

    override fun onCreate() {
        super.onCreate()

        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_SPEECH)
            .build()
        exoPlayer.setAudioAttributes(audioAttributes, true)

        // Monitor ExoPlayer events.
        exoPlayer.addListener(PlayerEventListener())

        // Setup notification and media session.
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            applicationContext,
            PLAYBACK_CHANNEL_ID,
            R.string.loading_book,
            PLAYBACK_NOTIFICATION_ID,
            object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): String {
                    return audioBookTitle ?: getString(R.string.loading_book)
                }

                override fun createCurrentContentIntent(player: Player): PendingIntent? =
                    PendingIntent.getActivity(
                        applicationContext,
                        0,
                        Intent(applicationContext, ActivityMain::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                override fun getCurrentContentText(player: Player): String? {
                    return null
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    return getBitmapFromVectorDrawable(applicationContext, R.drawable.play_icon)
                }
            },
            object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationStarted(
                    notificationId: Int,
                    notification: Notification?
                ) {
                    startForeground(notificationId, notification)
                }

                override fun onNotificationCancelled(notificationId: Int) {
                    _playerStatusFlow.tryEmit(PlayerStatus.Cancelled(audioBookId))
                    stopSelf()
                }

                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification?,
                    ongoing: Boolean
                ) {
                    if (ongoing) {
                        // Make sure the service will not get destroyed while playing media.
                        startForeground(notificationId, notification)
                    } else {
                        // Make notification cancellable.
                        stopForeground(false)
                    }
                }
            }
        ).apply {
            // Omit skip previous and next actions.
            setUseNavigationActions(false)

            // Add stop action.
            setUseStopAction(true)

            val incrementMs = resources.getInteger(R.integer.increment_ms).toLong()
            setFastForwardIncrementMs(incrementMs)
            setRewindIncrementMs(incrementMs)

            setPlayer(exoPlayer)
        }

        // Show lock screen controls and let apps like Google assistant manager playback.
        mediaSession = MediaSessionCompat(applicationContext, MEDIA_SESSION_TAG).apply {
            isActive = true
        }
        playerNotificationManager?.setMediaSessionToken(mediaSession?.sessionToken)

        mediaSessionConnector = MediaSessionConnector(mediaSession).apply {
            setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
                override fun getMediaDescription(
                    player: Player,
                    windowIndex: Int
                ): MediaDescriptionCompat {
                    val bitmap =
                        getBitmapFromVectorDrawable(applicationContext, R.drawable.play_icon)
                    val extras = Bundle().apply {
                        putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
                        putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap)
                    }

                    val title = audioBookTitle ?: getString(R.string.loading_book)

                    return MediaDescriptionCompat.Builder()
                        .setIconBitmap(bitmap)
                        .setTitle(title)
                        .setExtras(extras)
                        .build()
                }
            })

            val incrementMs = resources.getInteger(R.integer.increment_ms)
            setFastForwardIncrementMs(incrementMs)
            setRewindIncrementMs(incrementMs)

            setPlayer(exoPlayer)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        handleIntent(intent)
        return AudioServiceBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleIntent(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        cancelPlaybackMonitor()

        mediaSession?.release()
        mediaSessionConnector?.setPlayer(null)
        playerNotificationManager?.setPlayer(null)

        exoPlayer.release()

        super.onDestroy()
    }

    @MainThread
    private fun handleIntent(intent: Intent?) {
        // Play
        intent?.let {
            intent.getParcelableExtra<Uri>(ARG_URI)?.also { uri ->
                audioBookId = intent.getStringExtra(ARG_EPISODE_ID)
                audioBookTitle = intent.getStringExtra(ARG_TITLE)
                val startPosition =
                    intent.getLongExtra(ARG_START_POSITION, C.POSITION_UNSET.toLong())
                val playbackSpeed = playbackManager.playbackSpeed

                play(uri, startPosition, playbackSpeed)
            } ?: Log.w("Joseph", "Playback uri was not set")
        }
    }

    @MainThread
    fun play(uri: Uri, startPosition: Long, playbackSpeed: Float? = null) {
        val userAgent = Util.getUserAgent(applicationContext, BuildConfig.APPLICATION_ID)
        val mediaSource = ExtractorMediaSource(
            uri,
            DefaultDataSourceFactory(applicationContext, userAgent),
            DefaultExtractorsFactory(),
            null,
            null
        )

        val haveStartPosition = startPosition != C.POSITION_UNSET.toLong()
        if (haveStartPosition) exoPlayer.seekTo(startPosition)
        playbackSpeed?.let { changePlaybackSpeed(playbackSpeed) }
        exoPlayer.prepare(mediaSource, !haveStartPosition, false)
        exoPlayer.playWhenReady = true
    }

    @MainThread
    fun resume() {
        exoPlayer.playWhenReady = true
    }

    @MainThread
    fun pause() {
        exoPlayer.playWhenReady = false
    }

    @MainThread
    fun changePlaybackSpeed(playbackSpeed: Float) {
        exoPlayer.playbackParameters = PlaybackParameters(playbackSpeed)
    }

    @MainThread
    private fun saveLastListeningPosition() {
        if (audioBookId == null) return
        _updateAudioBookPosition.tryEmit(Pair(audioBookId!!, exoPlayer.contentPosition.toInt()))
    }

    @MainThread
    private fun monitorPlaybackProgress() {
        if (playbackTimer == null) {
            playbackTimer = Timer()
            playbackTimer?.scheduleAtFixedRate(
                createTimerTask(),
                PLAYBACK_TIMER_DELAY,
                PLAYBACK_TIMER_DELAY
            )
        }
    }

    private fun createTimerTask() = object : TimerTask() {
        override fun run() {
            saveLastListeningPosition()
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    if (exoPlayer.duration - exoPlayer.contentPosition <= PLAYBACK_TIMER_DELAY) {
                        playbackTimer?.cancel()
                    }
                }
            }
        }
    }

    @MainThread
    private fun cancelPlaybackMonitor() {
        saveLastListeningPosition()

        playbackTimer?.cancel()
        playbackTimer = null
    }

    @MainThread
    private fun getBitmapFromVectorDrawable(
        context: Context,
        @DrawableRes drawableId: Int
    ): Bitmap? {
        return ContextCompat.getDrawable(context, drawableId)?.let {
            val drawable = DrawableCompat.wrap(it).mutate()

            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            bitmap
        }
    }

    private inner class PlayerEventListener : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            val id = audioBookId ?: String()
            when (playbackState) {
                Player.STATE_READY -> handlePlaybackStateStateReady(id)
                Player.STATE_ENDED -> _playerStatusFlow.tryEmit(PlayerStatus.Ended(id))
                else -> _playerStatusFlow.tryEmit(PlayerStatus.Other(id))
            }
            // Only monitor playback to record progress when playing.
            if (playbackState == Player.STATE_READY && exoPlayer.playWhenReady) monitorPlaybackProgress()
            else cancelPlaybackMonitor()
        }

        override fun onPlayerError(e: ExoPlaybackException?) {
            audioBookId?.let { _playerStatusFlow.tryEmit(PlayerStatus.Error(it, e)) }
        }

        private fun handlePlaybackStateStateReady(episodeId: String) {
            if (exoPlayer.playWhenReady) _playerStatusFlow.tryEmit(PlayerStatus.Playing(episodeId))
            else _playerStatusFlow.tryEmit(PlayerStatus.Paused(episodeId))
        }
    }
}