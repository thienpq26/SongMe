package com.example.songme.data.source

interface TrackDataSource {
    interface Remote {
        fun getTracks(keyword: String, callback: OnTracksLoadedCallback)
        fun getGenres(genres: String, callback: OnTracksLoadedCallback)
        fun getTrending(callback: OnTracksLoadedCallback)
    }
}
