package com.example.songme.mediaplayer

import androidx.annotation.IntDef

@IntDef(
    StateType.IDLE,
    StateType.INITIALIZED,
    StateType.PREPARING,
    StateType.PREPARED,
    StateType.STARTED,
    StateType.PAUSED,
    StateType.STOPPED,
    StateType.END,
    StateType.PLAYBACK_COMPLETED
)
annotation class StateType {
    companion object {
        const val IDLE = 0
        const val INITIALIZED = 1
        const val PREPARING = 2
        const val PREPARED = 3
        const val STARTED = 4
        const val PAUSED = 5
        const val STOPPED = 6
        const val END = 7
        const val PLAYBACK_COMPLETED = 8
    }
}
