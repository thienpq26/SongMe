package com.example.songme.data.source.local

import android.content.ContentResolver
import com.example.songme.data.model.DataRequest
import com.example.songme.data.source.OnTracksLoadedCallback
import com.example.songme.data.source.TrackDataSource
import com.example.songme.utils.Constants.ALBUMART
import com.example.songme.utils.Constants.AUDIO
import com.example.songme.utils.Constants.CONTENT
import com.example.songme.utils.Constants.EXTERNAL
import com.example.songme.utils.Constants.MEDIA

class TrackLocalDataSource private constructor(private val resolver: ContentResolver) : TrackDataSource.Local {

    override fun getLocalTracks(callback: OnTracksLoadedCallback) {
        GetLocalAsync(TrackLocalHandler.getInstance(resolver), callback).execute(
            DataRequest(CONTENT, MEDIA, listOf(EXTERNAL, AUDIO, ALBUMART), mapOf()).toUrl()
        )
    }

    companion object {
        private var instance: TrackLocalDataSource? = null
        fun getInstance(resolver: ContentResolver): TrackLocalDataSource {
            return instance ?: TrackLocalDataSource(resolver).also { instance = it }
        }
    }
}
