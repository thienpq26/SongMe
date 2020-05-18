package com.example.songme.mediaplayer

import android.media.AudioManager
import android.media.MediaPlayer
import com.example.songme.data.model.Track
import com.example.songme.service.MusicService
import com.example.songme.service.ServiceContract
import com.example.songme.utils.Constants.DEFAULT_POSITION

class MediaPlayerManager private constructor(private val musicService: MusicService) :
    MediaPlayerContract {

    private var mediaPlayer: MediaPlayer? = null
    private var tracks: List<Track>? = null
    private var currentPosition: Int = 0
    private var onMediaPlayChange: ServiceContract.OnMediaPlayChange? = null

    fun setOnMediaChangeListener(onMediaPlayChange: ServiceContract.OnMediaPlayChange?) {
        this.onMediaPlayChange = onMediaPlayChange
    }

    override fun playTrack() {
        val track = currentPosition.let { tracks?.get(it) }
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaPlayer?.apply {
                setDataSource(track?.streamUrl)
                prepareAsync()
                setOnPreparedListener(musicService)
            }
        } catch (e: Exception) {
            TODO("NOT DONE")
        }
        onMediaPlayChange?.onTrackChange(track)
    }

    override fun pauseTrack() {
        val player = mediaPlayer ?: return
        if (player.isPlaying) {
            player.pause()
            onMediaPlayChange?.onMediaStateChange(false)
        } else {
            player.start()
            onMediaPlayChange?.onMediaStateChange(true)
        }
    }

    override fun nextTrack() {
        tracks?.size?.let { (currentPosition + 1) % it }?.let {
            currentPosition = it
        }
        playTrack()
    }

    override fun prevTrack() {
        tracks?.size?.let { (currentPosition + it - 1) % it }?.let {
            currentPosition = it
        }
        playTrack()
    }

    override fun changeLoop() {
        TODO("Not yet implemented")
    }

    override fun changeShuffled() {
        TODO("Not yet implemented")
    }

    override fun getTracks(): List<Track> = tracks!!

    override fun release() {
        mediaPlayer?.release()
    }

    override fun onPrepared() {
        mediaPlayer?.apply {
            start()
            setOnPreparedListener(musicService)
        }
        onMediaPlayChange?.onMediaStateChange(true)
    }

    override fun onCompletion() {
        onMediaPlayChange?.onMediaStateChange(false)
    }

    fun changeTrack(tracks: List<Track>, position: Int) {
        this.tracks = tracks
        this.currentPosition = position
    }

    override fun getCurrentTrack(): Track? = tracks?.get(currentPosition)

    override fun getDuration(): Int = mediaPlayer?.duration ?: DEFAULT_POSITION

    override fun getCurrentDuration(): Int = mediaPlayer?.currentPosition ?: DEFAULT_POSITION

    override fun seekTo(duration: Int) {
        mediaPlayer?.seekTo(duration)
    }

    companion object {
        fun newInstance(musicService: MusicService) = MediaPlayerManager(musicService)
    }
}
