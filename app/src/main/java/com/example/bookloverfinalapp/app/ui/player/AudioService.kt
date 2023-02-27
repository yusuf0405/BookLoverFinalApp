package com.example.bookloverfinalapp.app.ui.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.ui.general_screens.activity_main.ActivityMain
import com.example.bookloverfinalapp.app.utils.extensions.createSharedFlowAsLiveData
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


private const val PLAYBACK_CHANNEL_ID = "playback_channel"
private const val PLAYBACK_NOTIFICATION_ID = 2
private const val MEDIA_SESSION_TAG = "sed_audio"

private const val PLAYBACK_TIMER_DELAY = 5 * 1000L

private const val ARG_EPISODE_ID = "episode_id"
private const val ARG_URI = "uri_string"
private const val ARG_TITLE = "ARG_TITLE"
private const val ARG_POSTER_URL = "ARG_POSTER_URL"
private const val ARG_START_POSITION = "ARG_START_POSITION"
private const val CHANNEL_NAME = "My Background Service"

@AndroidEntryPoint
class AudioService : LifecycleService() {

    inner class AudioServiceBinder : Binder() {
        val service
            get() = this@AudioService

        val exoPlayer
            get() = this@AudioService.exoPlayer
    }

    companion object {

        fun newIntent(context: Context, audioBook: AudioBook? = null) =
            Intent(context, AudioService::class.java).apply {
                audioBook?.let {
                    putExtra(ARG_EPISODE_ID, audioBook.id)
                    putExtra(ARG_TITLE, audioBook.title)
                    putExtra(ARG_POSTER_URL, audioBook.audioBookPoster.url)
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
    private var audioBookPosterUrl: String? = null
    private var posterBitmap: Bitmap? = null

    private val exoPlayer: ExoPlayer by lazy(LazyThreadSafetyMode.NONE) {
        ExoPlayer.Builder(applicationContext).build()
    }

    private var playerNotificationManager: PlayerNotificationManager? = null
    private var mediaSession: MediaSessionCompat? = null
    private var mediaSessionConnector: MediaSessionConnector? = null

    private val _playerStatusFlow = createSharedFlowAsLiveData<PlayerStatus>()
    val playerStatusFlow: SharedFlow<PlayerStatus> get() = _playerStatusFlow.asSharedFlow()

    override fun onCreate() {
        super.onCreate()

        val audioAttributes = AudioAttributes.Builder().setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH).build()
        exoPlayer.setAudioAttributes(audioAttributes, true)

        // Monitor ExoPlayer events.
        exoPlayer.addListener(PlayerEventListener())

        val emptyNotification: Notification = emptyNotification(notificationManager())
        startForeground(PLAYBACK_NOTIFICATION_ID, emptyNotification)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel()

        playerNotificationManager = PlayerNotificationManager.Builder(
            applicationContext,
            PLAYBACK_NOTIFICATION_ID,
            PLAYBACK_CHANNEL_ID
        ).setMediaDescriptionAdapter(createMediaDescriptionAdapter())
            .setNotificationListener(createNotificationListener())
            .build()
            .apply(::setupPlayerNotificationManager)
        // Show lock screen controls and let apps like Google assistant manager playback.
        mediaSession = MediaSessionCompat(
            applicationContext,
            MEDIA_SESSION_TAG
        ).apply { isActive = true }

        val mediaSession = mediaSession ?: return
        playerNotificationManager?.setMediaSessionToken(mediaSession.sessionToken)
        mediaSessionConnector = MediaSessionConnector(mediaSession)
            .apply(::setupMediaSessionConnector)
    }

    private fun setupMediaSessionConnector(
        mediaSessionConnector: MediaSessionConnector,
    ) = with(mediaSessionConnector) {
        val mediaSession = mediaSession ?: return
        setQueueNavigator(createTimelineQueueNavigator(mediaSession))
        setPlayer(exoPlayer)
    }

    private fun setupPlayerNotificationManager(
        manager: PlayerNotificationManager
    ) = with(manager) {
        setUseFastForwardAction(true)
        setUseFastForwardActionInCompactView(true)
        setUseRewindAction(true)
        setUseRewindActionInCompactView(true)
        setPlayer(exoPlayer)
        setUseStopAction(true)
    }

    private fun createNotificationListener() =
        object : PlayerNotificationManager.NotificationListener {

            override fun onNotificationCancelled(
                notificationId: Int,
                dismissedByUser: Boolean
            ) {
                _playerStatusFlow.tryEmit(PlayerStatus.Cancelled(audioBookId))
                stopSelf()
            }

            override fun onNotificationPosted(
                notificationId: Int,
                notification: Notification,
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

    private fun createMediaDescriptionAdapter(
    ) = object : PlayerNotificationManager.MediaDescriptionAdapter {

        override fun getCurrentContentTitle(
            player: Player
        ): String = audioBookTitle ?: getString(R.string.loading_book)

        override fun createCurrentContentIntent(
            player: Player
        ): PendingIntent = createOpenAppIntent()

        override fun getCurrentContentText(player: Player): String? = null

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? = getBitmapFromVectorDrawable(
            applicationContext,
            R.drawable.background_start
        )
    }

    private fun createTimelineQueueNavigator(
        mediaSession: MediaSessionCompat
    ) = object : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(
            player: Player, windowIndex: Int
        ): MediaDescriptionCompat {
            createBitmapFromPosterUrl()
            val extras = Bundle().apply {
                putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, posterBitmap)
                putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, posterBitmap)
            }

            val title = audioBookTitle ?: getString(R.string.loading_book)
            return MediaDescriptionCompat.Builder()
                .setIconBitmap(posterBitmap)
                .setTitle(title)
                .setExtras(extras).build()
        }
    }

    private fun createBitmapFromPosterUrl() {
        Glide.with(applicationContext)
            .asBitmap()
            .load(audioBookPosterUrl)
            .into(createCustomTarget())
    }

    private fun createCustomTarget() = object : CustomTarget<Bitmap>() {
        override fun onResourceReady(
            resource: Bitmap,
            transition: Transition<in Bitmap>?,
        ) {
            posterBitmap = resource
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            posterBitmap = getBitmapFromVectorDrawable(applicationContext, R.drawable.play_icon)
        }
    }

    private fun emptyNotification(
        notificationManager: NotificationManager,
    ): Notification {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            PLAYBACK_CHANNEL_ID
        )
        builder.setSmallIcon(R.drawable.play_icon)
        val notification = builder.build()
        notificationManager.notify(PLAYBACK_NOTIFICATION_ID, notification)
        return notification
    }

    private fun notificationManager(): NotificationManager =
        getSystemService(NOTIFICATION_SERVICE).run {
            if (this is NotificationManager) this
            else throw IllegalStateException()
        }


    private fun getImmutableFlag() =
        if (isAndroidAPI31()) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else 0

    private fun isAndroidAPI31() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    private fun createOpenAppIntent(): PendingIntent {
        val intent = Intent(applicationContext, ActivityMain::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(applicationContext, 0, intent, getImmutableFlag())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val chan = NotificationChannel(
            PLAYBACK_CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
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


    private fun handleIntent(intent: Intent?) {
        // Play
        intent?.let {
            intent.getParcelableExtra<Uri>(ARG_URI)?.also { uri ->
                audioBookId = intent.getStringExtra(ARG_EPISODE_ID)
                audioBookTitle = intent.getStringExtra(ARG_TITLE)
                audioBookPosterUrl = intent.getStringExtra(ARG_POSTER_URL)
                val startPosition =
                    intent.getLongExtra(ARG_START_POSITION, C.POSITION_UNSET.toLong())
                val playbackSpeed = playbackManager.playbackSpeed

                play(uri, startPosition, playbackSpeed)
            } ?: Log.w("Joseph", "Playback uri was not set")
        }
    }

    private fun play(uri: Uri, startPosition: Long, playbackSpeed: Float? = null) {
        val mediaSource = ProgressiveMediaSource
            .Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(uri))

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
    private fun monitorPlaybackProgress() {
        if (playbackTimer == null) {
            playbackTimer = Timer()
            playbackTimer?.scheduleAtFixedRate(
                createTimerTask(), PLAYBACK_TIMER_DELAY, PLAYBACK_TIMER_DELAY
            )
        }
    }

    private fun createTimerTask() = object : TimerTask() {
        override fun run() {
            handleRunTimer()
        }
    }

    private fun handleRunTimer() {
        lifecycleScope.launch(Dispatchers.Main) {
            if (exoPlayer.duration - exoPlayer.contentPosition <= PLAYBACK_TIMER_DELAY) {
                playbackTimer?.cancel()
            }
        }
    }


    @MainThread
    private fun cancelPlaybackMonitor() {
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
                drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            bitmap
        }
    }

    private inner class PlayerEventListener : Player.Listener {
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

        override fun onPlayerError(error: PlaybackException) {
            audioBookId?.let { _playerStatusFlow.tryEmit(PlayerStatus.Error(it, error)) }
        }

        private fun handlePlaybackStateStateReady(episodeId: String) {
            if (exoPlayer.playWhenReady) _playerStatusFlow.tryEmit(PlayerStatus.Playing(episodeId))
            else _playerStatusFlow.tryEmit(PlayerStatus.Paused(episodeId))
        }
    }

    override fun onDestroy() {
        cancelPlaybackMonitor()
        mediaSession?.release()
        mediaSessionConnector?.setPlayer(null)
        playerNotificationManager?.setPlayer(null)
        exoPlayer.release()
        super.onDestroy()
    }
}