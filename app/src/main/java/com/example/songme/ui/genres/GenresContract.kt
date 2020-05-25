package com.example.songme.ui.genres

import com.example.songme.data.model.Track

interface GenresContract {
    interface View {
        fun showGenres(genre: List<Track>)
        fun showError(exception: Exception)
    }

    interface Presenter {
        fun getTracksByGenre(genre: String)
    }
}
