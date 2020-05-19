package com.example.songme.mediaplayer

import android.app.DownloadManager
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import com.example.songme.data.model.Track
import com.example.songme.service.MusicService
import com.example.songme.service.ServiceContract
import com.example.songme.utils.Constants.DEFAULT_POSITION
import com.example.songme.utils.Constants.MP3_EXTENSION
import com.example.songme.utils.Constants.POSITION_VALUE_ONE
import java.util.*
import kotlin.collections.ArrayList

class MediaPlayerManager private constructor(private val musicService: MusicService) :
    MediaPlayerContract {

    private var mediaPlayer: MediaPlayer? = null
    private var tracks: MutableList<Track>? = null
    private var shuffleTracks: List<Track>? = null
    private var temporaryTracks: List<Track>? = null
    private var currentPosition: Int = 0
    private var state: Int? = null
    private var loop: Int? = null
    private var downloadManager: DownloadManager? = null
    private var onMediaPlayChange: ServiceContract.OnMediaPlayChange? = null

    fun setOnMediaChangeListener(onMediaPlayChange: ServiceContract.OnMediaPlayChange?) {
        this.onMediaPlayChange = onMediaPlayChange
    }

    override fun playTrack() {
        if (state != StateType.END) {
            val track = currentPosition.let { tracks?.get(it) }
            mediaPlayer?.apply {
                release()
                state = StateType.END
            }
            mediaPlayer = MediaPlayer()
            state = StateType.IDLE
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                mediaPlayer?.apply {
                    setDataSource(track?.streamUrl)
                    state = StateType.INITIALIZED
                    prepareAsync()
                    state = StateType.PREPARING
                    setOnPreparedListener(musicService)
                }
            } catch (e: Exception) {
                nextTrack()
            }
            track?.isDownload.let {
                onMediaPlayChange?.isDownload(it)
            }
            onMediaPlayChange?.onTrackChange(track)
        }
    }

    override fun pauseTrack() {
        val player = mediaPlayer ?: return
        if (state == StateType.STARTED) {
            player.pause()
            state = StateType.PAUSED
            onMediaPlayChange?.onMediaStateChange(false)
        } else if (state == StateType.PREPARING || state == StateType.PAUSED || state == StateType.PLAYBACK_COMPLETED) {
            player.start()
            state = StateType.STARTED
            onMediaPlayChange?.onMediaStateChange(true)
        }
    }

    override fun nextTrack() {
        onMediaPlayChange?.onMediaStateChange(false)
        tracks?.size?.let { (currentPosition + 1) % it }?.let {
            currentPosition = it
        }
        playTrack()
    }

    override fun prevTrack() {
        onMediaPlayChange?.onMediaStateChange(false)
        tracks?.size?.let { (currentPosition + it - 1) % it }?.let {
            currentPosition = it
        }
        playTrack()
    }

    override fun changeLoop() {
        when (loop) {
            LoopType.NO -> loop = LoopType.ALL
            LoopType.ALL -> loop = LoopType.ONE
            LoopType.ONE -> loop = LoopType.NO
        }
        loop?.let { onMediaPlayChange?.setLoop(it) }
    }

    override fun turnOnShuffledMode() {
        val tracks = tracks ?: return
        val shuffleTrack = shuffleTracks ?: return
        currentPosition = tracks[currentPosition].let { shuffleTrack.indexOf(it) }
        tracks.apply {
            clear()
            addAll(shuffleTrack)
        }
        onMediaPlayChange?.setShuffle(true)
    }

    override fun turnOffShuffledMode() {
        val tracks = tracks ?: return
        val temporaryTracks = temporaryTracks ?: return
        currentPosition = tracks[currentPosition].let { temporaryTracks.indexOf(it) }
        tracks.apply {
            clear()
            addAll(temporaryTracks)
        }
        onMediaPlayChange?.setShuffle(false)
    }

    override fun getTracks(): List<Track> = tracks!!

    override fun release() {
        mediaPlayer?.release()
        state = StateType.END
    }

    override fun onPrepared() {
        if (state == StateType.PREPARING) {
            mediaPlayer?.apply {
                start()
                state = StateType.STARTED
                setOnCompletionListener(musicService)
            }
            onMediaPlayChange?.onMediaStateChange(true)
        }
    }

    override fun onCompletion() {
        if (state == StateType.STARTED) {
            state = StateType.PLAYBACK_COMPLETED
            when (loop) {
                LoopType.ALL -> nextTrack()
                LoopType.ONE -> playTrack()
                LoopType.NO -> {
                    tracks?.let {
                        if (currentPosition != it.size - POSITION_VALUE_ONE) {
                            nextTrack()
                        }
                    }
                }
            }
            onMediaPlayChange?.onMediaStateChange(false)
        }
    }

    override fun downloadTrack() {
        downloadManager = musicService.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager?.enqueue(
            DownloadManager.Request(Uri.parse(tracks?.get(currentPosition)?.streamUrl))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_MUSIC,
                    tracks?.get(currentPosition)?.title + MP3_EXTENSION
                )
                .setDescription(tracks?.get(currentPosition)?.title)
        )
        onMediaPlayChange?.onDownloadSucceeded()
    }

    fun changeTrack(tracks: MutableList<Track>, position: Int) {
        loop = LoopType.NO
        this.tracks = tracks
        this.currentPosition = position
        shuffleTracks = ArrayList(tracks)
        temporaryTracks = ArrayList(tracks)
        Collections.shuffle(shuffleTracks)
    }

    override fun getCurrentTrack(): Track? = tracks?.get(currentPosition)

    override fun getDuration(): Int {
        state?.let {
            if (it >= StateType.STARTED) {
                mediaPlayer?.apply {
                    return duration
                }
            }
        }
        return DEFAULT_POSITION
    }

    override fun getCurrentDuration(): Int {
        state?.let {
            if (it >= StateType.STARTED) {
                mediaPlayer?.apply { return currentPosition }
            }
        }
        return DEFAULT_POSITION
    }

    override fun seekTo(duration: Int) {
        mediaPlayer?.seekTo(duration)
    }

    companion object {
        fun newInstance(musicService: MusicService) = MediaPlayerManager(musicService)
    }
}
