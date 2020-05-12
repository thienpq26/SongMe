package com.example.songme.ui.mymusic

import com.example.songme.data.model.Track
import com.example.songme.data.repository.TrackRepository
import com.example.songme.data.source.OnTracksLoadedCallback
import java.lang.Exception

class MyMusicPresenter(
    private val view: MyMusicContract.View,
    private val repository: TrackRepository
) : MyMusicContract.Presenter {

    override fun getLocalTracks() {
        repository.getLocalTracks(object : OnTracksLoadedCallback {
            override fun onSucceeded(data: List<Track>) {
                view.showTracks(data)
            }

            override fun onFailed(exception: Exception) {
                view.showError(exception)
            }
        })
    }
}
