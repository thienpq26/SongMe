package com.example.songme.data.source.remote.ResponceFromAPI

import com.example.songme.data.model.Track
import com.example.songme.utils.*
import com.example.songme.utils.Constants.CONNECT_TIMEOUT
import com.example.songme.utils.Constants.READ_TIMEOUT
import com.example.songme.utils.Constants.REQUEST_METHOD
import org.json.JSONArray
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

private const val MESSSAGE_ERROR = "Connection error"

class TrackResponseHandler private constructor() : DataResponseHandler {

    override fun jsonToListTrack(jsonData: String): List<Track> {
        return convertJsonToTracks(jsonData)
    }

    override fun getResponse(url: String): List<Track> {
        val responseData: List<Track>
        var connection: HttpURLConnection? = null
        try {
            connection = (URL(url).openConnection() as HttpURLConnection).apply {
                requestMethod = REQUEST_METHOD
                connectTimeout = CONNECT_TIMEOUT
                readTimeout = READ_TIMEOUT
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw IOException(MESSSAGE_ERROR)
                }
                responseData = jsonToListTrack(InputStreamReader(inputStream).getJsonString())
            }
        } finally {
            connection?.disconnect()
        }
        return responseData
    }

    override fun convertJsonToTracks(jsonData: String): List<Track> {
        val tracks = mutableListOf<Track>()
        val jsonArray = JSONArray(jsonData)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            tracks.add(Track(jsonObject))
        }
        return tracks
    }

    companion object {
        private var singleInstance: TrackResponseHandler? = null
        fun getInstance(): TrackResponseHandler {
            return singleInstance ?: TrackResponseHandler().also { singleInstance = it }
        }
    }
}
