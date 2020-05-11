package com.example.songme.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.songme.R
import com.example.songme.data.model.Track
import com.example.songme.data.repository.TrackRepository
import com.example.songme.data.source.remote.TrackRemoteDataSource
import com.example.songme.ui.adapter.TrackAdapter
import com.example.songme.utils.Constants.FLAG_REMOTE
import com.example.songme.utils.showMessage
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment private constructor() : Fragment(), HomeContentContract.View {

    private val presenter: HomeContentContract.Presenter by lazy {
        HomeContentPresenter(
            this,
            TrackRepository.getInstance(TrackRemoteDataSource.getInstance())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getTrending()
    }

    override fun showTracks(tracks: List<Track>) {
        recyclerTrending.adapter = TrackAdapter(FLAG_REMOTE, { item, position ->
            TODO("Handle Click on recycler view item")
        }).apply { updateData(tracks) }
    }

    override fun showError(exception: Exception) {
        context?.showMessage(exception.message.toString())
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
