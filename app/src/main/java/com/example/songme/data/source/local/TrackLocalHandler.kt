package com.example.songme.data.source.local

import android.content.ContentResolver
import android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
import com.example.songme.data.model.Track

class TrackLocalHandler private constructor(private val resolver: ContentResolver) : DataLocalHandler {

    override fun getTracks(uri: String): List<Track> {
        val tracks = mutableListOf<Track>()
        val cursor = resolver.query(EXTERNAL_CONTENT_URI, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                tracks.add(Track(cursor, uri))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return tracks
    }

    companion object {
        private var instance: TrackLocalHandler? = null
        fun getInstance(resolver: ContentResolver): TrackLocalHandler {
            return instance ?: TrackLocalHandler(resolver).also { instance = it }
        }
    }
}
