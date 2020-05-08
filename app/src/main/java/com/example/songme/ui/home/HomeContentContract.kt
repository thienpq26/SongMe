package com.example.songme.ui.home

import com.example.songme.data.model.Track

interface HomeContentContract {
    interface View {
        fun showTracks(tracks: List<Track>)
        fun showError(exception: Exception)
    }

    interface Presenter {
        fun getTrending()
    }
}
