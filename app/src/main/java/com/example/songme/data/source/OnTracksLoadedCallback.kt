package com.example.songme.data.source

import com.example.songme.data.model.Track
import java.lang.Exception

interface OnTracksLoadedCallback {
    fun onSucceeded(data: List<Track>)
    fun onFailed(exception: Exception)
}
