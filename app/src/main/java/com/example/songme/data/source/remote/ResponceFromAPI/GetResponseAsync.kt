package com.example.songme.data.source.remote.ResponceFromAPI

import android.os.AsyncTask
import com.example.songme.data.model.Track
import com.example.songme.data.source.OnTracksLoadedCallback

class GetResponseAsync(
    private val dataResponseHandler: DataResponseHandler,
    private val callback: OnTracksLoadedCallback
) : AsyncTask<String, Void, List<Track>>() {

    private var exception: Exception? = null

    override fun doInBackground(vararg params: String): List<Track>? =
        try {
            dataResponseHandler.getResponse(params[0])
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
