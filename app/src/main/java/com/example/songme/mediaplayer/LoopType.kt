package com.example.songme.mediaplayer

import androidx.annotation.IntDef

@IntDef(LoopType.ALL, LoopType.NO, LoopType.ONE)
annotation class LoopType {
    companion object {
        const val NO = 0
        const val ONE = 1
        const val ALL = 2
    }
}
