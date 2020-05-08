package com.example.songme.utils

import java.io.BufferedReader
import java.io.InputStreamReader

fun InputStreamReader.getJsonString(): String {
    val reader = BufferedReader(this)
    return StringBuilder().apply {
        var inputLine = reader.readLine()
        while (inputLine != null) {
            append(inputLine)
            inputLine = reader.readLine()
        }
        reader.close()
    }.toString()
}
