package com.example.songme.data.source.local

import android.os.AsyncTask
import com.example.songme.data.model.Track
import com.example.songme.data.source.OnTracksLoadedCallback

class GetLocalAsync(
    private val localHandler: DataLocalHandler,
    private val callback: OnTracksLoadedCallback
) : AsyncTask<String, Void, List<Track>>() {

    private var exception: Exception? = null

    override fun doInBackground(vararg params: String): List<Track>? =
        try {
            localHandler.getTracks(params[0])
        } catch (exception: Exception) {
            this.exception = exception
            null
        }

    override fun onPostExecute(result: List<Track>?) {
        result?.let {
            callback.onSucceeded(it)
        } ?: exception?.let {
            callback.onFailed(it)
        }
    }
}
