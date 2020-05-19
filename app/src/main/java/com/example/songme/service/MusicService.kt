package com.example.songme.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.Parcelable
import com.example.songme.data.model.Track
import com.example.songme.mediaplayer.MediaPlayerManager
import com.example.songme.utils.Constants.DEFAULT_POSITION
import com.example.songme.utils.Constants.EXTRA_TRACKS_LIST
import com.example.songme.utils.Constants.EXTRA_TRACK_POSITION
import java.util.*

class MusicService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private var binder: TrackBinder? = null
    private var mediaPlayerManager: MediaPlayerManager? = null

    override fun onCreate() {
        super.onCreate()
        binder = TrackBinder()
        mediaPlayerManager = MediaPlayerManager.newInstance(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getParcelableArrayListExtra<Track>(EXTRA_TRACKS_LIST)?.let {
            mediaPlayerManager?.apply {
                changeTrack(it, intent.getIntExtra(EXTRA_TRACK_POSITION, DEFAULT_POSITION))
                playTrack()
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = binder

    fun setOnMediaChangeListener(onMediaPlayChange: ServiceContract.OnMediaPlayChange?) {
        mediaPlayerManager?.setOnMediaChangeListener(onMediaPlayChange)
    }

    fun pauseTrack() {
        mediaPlayerManager?.pauseTrack()
    }

    fun shuffleTrack(isShuffle: Boolean) {
        mediaPlayerManager?.apply {
            if (isShuffle) turnOnShuffledMode()
            else turnOffShuffledMode()
        }
    }

    fun nextTrack() {
        mediaPlayerManager?.nextTrack()
    }

    fun prevTrack() {
        mediaPlayerManager?.prevTrack()
    }

    fun getCurrentDuration(): Int? = mediaPlayerManager?.getCurrentDuration()

    fun getDuration(): Int? = mediaPlayerManager?.getDuration()

    fun seekTo(duration: Int) {
        mediaPlayerManager?.seekTo(duration)
    }

    fun downloadTrack() {
        mediaPlayerManager?.downloadTrack()
    }

    fun getCurrentTrack(): Track? = mediaPlayerManager?.getCurrentTrack()

    fun getTracks(): List<Track?>? = mediaPlayerManager?.getTracks()

    fun changeLoop() {
        mediaPlayerManager?.changeLoop()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayerManager?.onPrepared()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mediaPlayerManager?.onCompletion()
    }

    override fun onDestroy() {
        mediaPlayerManager?.release()
        super.onDestroy()
    }

    inner class TrackBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, MusicService::class.java)
        fun getIntent(context: Context, tracks: List<Track>, position: Int) =
            Intent(context, MusicService::class.java)
                .putParcelableArrayListExtra(
                    EXTRA_TRACKS_LIST,
                    tracks as ArrayList<out Parcelable?>?
                )
                .putExtra(EXTRA_TRACK_POSITION, position)
    }
}
