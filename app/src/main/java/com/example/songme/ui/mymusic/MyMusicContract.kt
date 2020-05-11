package com.example.songme.ui.mymusic

import com.example.songme.data.model.Track

interface MyMusicContract {
    interface View {
        fun showTracks(tracks: List<Track>)
        fun showError(exception: Exception)
        fun showActionTrack()
    }

    interface Presenter {
        fun getLocalTracks()
    }
}
