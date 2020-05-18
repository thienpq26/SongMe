package com.example.songme.service

import com.example.songme.data.model.Track

interface ServiceContract {
    interface OnMediaPlayChange {
        fun onTrackChange(track: Track?)
        fun onMediaStateChange(isPlaying: Boolean)
    }
}
