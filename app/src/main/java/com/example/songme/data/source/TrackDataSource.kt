package com.example.songme.data.source

interface TrackDataSource {
    interface Remote {
        fun getRemoteTracks(keyword: String, callback: OnTracksLoadedCallback)
        fun getGenres(genres: String, callback: OnTracksLoadedCallback)
        fun getTrending(callback: OnTracksLoadedCallback)
    }

    interface Local {
        fun getLocalTracks(callback: OnTracksLoadedCallback)
    }
}
