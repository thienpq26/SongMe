package com.example.songme.ui.genres

import com.example.songme.data.model.Track
import com.example.songme.data.repository.TrackRepository
import com.example.songme.data.source.OnTracksLoadedCallback
import java.lang.Exception

class GenresPresenter(
    private val view: GenresContract.View,
    private val repository: TrackRepository
) : GenresContract.Presenter {
    override fun getTracksByGenre(genre: String) {
        repository.getGenres(genre, object : OnTracksLoadedCallback {
            override fun onSucceeded(data: List<Track>) {
                view.showGenres(data)
            }

            override fun onFailed(exception: Exception) {
                view.showError(exception)
            }
        })
    }
}
