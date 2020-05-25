package com.example.songme.ui.genres

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.songme.R
import com.example.songme.data.model.Track
import com.example.songme.data.repository.TrackRepository
import com.example.songme.data.source.local.TrackLocalDataSource
import com.example.songme.data.source.remote.TrackRemoteDataSource
import com.example.songme.service.MusicService
import com.example.songme.ui.adapter.TrackAdapter
import com.example.songme.utils.Constants.FLAG_LOCAL
import com.example.songme.utils.showMessage
import kotlinx.android.synthetic.main.fragment_genre.*

class GenreFragment : Fragment(), GenresContract.View {

    private var genre: String? = null
    private val presenter: GenresContract.Presenter by lazy {
        GenresPresenter(
            this,
            TrackRepository.getInstance(
                TrackLocalDataSource.getInstance(activity!!.contentResolver),
                TrackRemoteDataSource.getInstance()
            )
        )
    }

    private lateinit var callback: TrackAdapter.OnSendDataSelectedListener

    fun setOnSendDataSelectedListener(callback: TrackAdapter.OnSendDataSelectedListener) {
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        genre = arguments?.getString(BUNDLE_GENRES)
        return inflater.inflate(R.layout.fragment_genre, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genre?.let { presenter.getTracksByGenre(it) }
    }

    override fun showGenres(genre: List<Track>) {
        recyclerGenre?.apply {
            setHasFixedSize(true)
            adapter = TrackAdapter(FLAG_LOCAL,
                { item, position ->
                    activity?.let { it.startService(MusicService.getIntent(it, item, position)) }
                    callback.sendData(item, position)
                }).apply { updateData(genre) }
        }
    }

    override fun showError(exception: Exception) {
        context?.showMessage(exception.message.toString())
    }

    companion object {
        private const val BUNDLE_GENRES = "BUNDLE_GENRES"
        fun newInstance(genre: String) = GenreFragment().apply {
            arguments = bundleOf(BUNDLE_GENRES to genre)
        }
    }
}
