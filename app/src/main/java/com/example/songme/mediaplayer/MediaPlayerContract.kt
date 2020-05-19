package com.example.songme.mediaplayer

import com.example.songme.data.model.Track

interface MediaPlayerContract {
    fun getCurrentTrack(): Track?
    fun getDuration(): Int
    fun getCurrentDuration(): Int
    fun seekTo(duration: Int)
    fun playTrack()
    fun pauseTrack()
    fun nextTrack()
    fun prevTrack()
    fun changeLoop()
    fun turnOnShuffledMode()
    fun turnOffShuffledMode()
    fun getTracks(): List<Track>
    fun release()
    fun onPrepared()
    fun onCompletion()
    fun downloadTrack()
}
