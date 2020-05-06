package com.example.songme.data.source.remote.ResponceFromAPI

import com.example.songme.data.model.Track

interface DataResponseHandler {
    fun jsonToListTrack(jsonData: String): List<Track>
    fun getResponse(url: String): List<Track>
    fun convertJsonToTracks(jsonData: String): List<Track>
}
