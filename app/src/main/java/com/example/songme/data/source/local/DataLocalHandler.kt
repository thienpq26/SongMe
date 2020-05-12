package com.example.songme.data.source.local

import com.example.songme.data.model.Track

interface DataLocalHandler {
    fun getTracks(uri: String): List<Track>
}
