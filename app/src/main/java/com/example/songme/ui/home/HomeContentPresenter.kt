package com.example.songme.ui.home

import com.example.songme.data.model.Track
import com.example.songme.data.repository.TrackRepository
import com.example.songme.data.source.OnTracksLoadedCallback
import java.lang.Exception

class HomeContentPresenter(
    private val view: HomeContentContract.View,
    private val repository: TrackRepository
) : HomeContentContract.Presenter {

    override fun getTrending() {
        repository.getTrending(object : OnTracksLoadedCallback {
            override fun onSucceeded(data: List<Track>) {
                view.showTracks(data)
            }

            override fun onFailed(exception: Exception) {
                view.showError(exception)
            }
        })
    }
}
