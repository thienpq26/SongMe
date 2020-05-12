package com.example.songme.data.repository

import com.example.songme.data.source.OnTracksLoadedCallback
import com.example.songme.data.source.TrackDataSource

class TrackRepository private constructor(
    private val local: TrackDataSource.Local,
    private val remote: TrackDataSource.Remote
) : TrackDataSource.Local, TrackDataSource.Remote {

    override fun getLocalTracks(callback: OnTracksLoadedCallback) {
        local.getLocalTracks(callback)
    }

    override fun getRemoteTracks(keyword: String, callback: OnTracksLoadedCallback) {
        remote.getRemoteTracks(keyword, callback)
    }

    override fun getGenres(genres: String, callback: OnTracksLoadedCallback) {
        remote.getGenres(genres, callback)
    }

    override fun getTrending(callback: OnTracksLoadedCallback) {
        remote.getTrending(callback)
    }

    companion object {
        private var instance: TrackRepository? = null
        fun getInstance(
            local: TrackDataSource.Local,
            remote: TrackDataSource.Remote
        ): TrackRepository {
            return instance ?: TrackRepository(local, remote).also { instance = it }
        }
    }
}
