package com.example.songme.data.source.remote

import com.example.songme.BuildConfig
import com.example.songme.data.model.DataRequest
import com.example.songme.data.source.OnTracksLoadedCallback
import com.example.songme.data.source.TrackDataSource
import com.example.songme.data.source.remote.ResponceFromAPI.GetResponseAsync
import com.example.songme.data.source.remote.ResponceFromAPI.TrackResponseHandler
import com.example.songme.utils.Constants.AUTHORITY_SOUNDCLOUD
import com.example.songme.utils.Constants.CLIENT_ID
import com.example.songme.utils.Constants.GENRES
import com.example.songme.utils.Constants.KEYWORD
import com.example.songme.utils.Constants.PATH_TRACK
import com.example.songme.utils.Constants.SCHEME_HTTP

class TrackRemoteDataSource private constructor() : TrackDataSource.Remote {

    override fun getTracks(keyword: String, callback: OnTracksLoadedCallback) {
        GetResponseAsync(
            TrackResponseHandler.getInstance(),
            callback
        ).execute(
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
        GetResponseAsync(
            TrackResponseHandler.getInstance(),
            callback
        ).execute(
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
        GetResponseAsync(
            TrackResponseHandler.getInstance(),
            callback
        ).execute(
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
        private var singleInstance: TrackRemoteDataSource? = null
        fun getInstance(): TrackRemoteDataSource {
            return singleInstance ?: TrackRemoteDataSource().also { singleInstance = it }
        }
    }
}
