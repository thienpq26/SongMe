package com.example.songme.data.repository

import com.example.songme.data.source.OnTracksLoadedCallback
import com.example.songme.data.source.TrackDataSource
import com.example.songme.data.source.remote.TrackRemoteDataSource

class TrackRepository private constructor(private val dataSource: TrackDataSource.Remote) :
    TrackDataSource.Remote {

    override fun getTracks(keyword: String, callback: OnTracksLoadedCallback) {
        dataSource.getTracks(keyword, callback)
    }

    override fun getGenres(genres: String, callback: OnTracksLoadedCallback) {
        dataSource.getGenres(genres, callback)
    }

    override fun getTrending(callback: OnTracksLoadedCallback) {
        dataSource.getTrending(callback)
    }

    companion object {
        private var singleInstance: TrackRepository? = null
        fun getInstance(dataSource: TrackRemoteDataSource): TrackRepository {
            return singleInstance ?: TrackRepository(dataSource).also { singleInstance = it }
        }
    }
}
