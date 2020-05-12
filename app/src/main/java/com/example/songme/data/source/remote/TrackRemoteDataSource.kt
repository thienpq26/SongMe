package com.example.songme.data.source.remote

import com.example.songme.BuildConfig
import com.example.songme.data.model.DataRequest
import com.example.songme.data.source.OnTracksLoadedCallback
import com.example.songme.data.source.TrackDataSource
import com.example.songme.data.source.remote.ResponceFromAPI.GetResponseAsync
import com.example.songme.data.source.remote.ResponceFromAPI.TrackResponseHandler
import com.example.songme.utils.RemoteConstants.AUTHORITY_SOUNDCLOUD
import com.example.songme.utils.RemoteConstants.CLIENT_ID
import com.example.songme.utils.RemoteConstants.GENRES
import com.example.songme.utils.RemoteConstants.KEYWORD
import com.example.songme.utils.RemoteConstants.PATH_TRACK
import com.example.songme.utils.RemoteConstants.SCHEME_HTTP

class TrackRemoteDataSource private constructor() : TrackDataSource.Remote {

    override fun getRemoteTracks(keyword: String, callback: OnTracksLoadedCallback) {
        GetResponseAsync(TrackResponseHandler.getInstance(), callback).execute(
            DataRequest(
                SCHEME_HTTP,
                AUTHORITY_SOUNDCLOUD,
                listOf(PATH_TRACK),
                mapOf(
                    CLIENT_ID to BuildConfig.API_KEY,
                    KEYWORD to keyword
                )
            ).toUrl()
        )
    }

    override fun getGenres(genres: String, callback: OnTracksLoadedCallback) {
        GetResponseAsync(TrackResponseHandler.getInstance(), callback).execute(
            DataRequest(
                SCHEME_HTTP,
                AUTHORITY_SOUNDCLOUD,
                listOf(PATH_TRACK),
                mapOf(
                    CLIENT_ID to BuildConfig.API_KEY,
                    GENRES to genres
                )
            ).toUrl()
        )
    }

    override fun getTrending(callback: OnTracksLoadedCallback) {
        GetResponseAsync(TrackResponseHandler.getInstance(), callback).execute(
            DataRequest(
                SCHEME_HTTP,
                AUTHORITY_SOUNDCLOUD,
                listOf(PATH_TRACK),
                mapOf(
                    CLIENT_ID to BuildConfig.API_KEY
                )
            ).toUrl()
        )
    }

    companion object {
        private var instance: TrackRemoteDataSource? = null
        fun getInstance(): TrackRemoteDataSource {
            return instance ?: TrackRemoteDataSource().also { instance = it }
        }
    }
}
