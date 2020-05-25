package com.example.songme.ui.playmusic

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.songme.R
import com.example.songme.data.model.Track
import com.example.songme.service.MusicService
import com.example.songme.ui.adapter.TrackAdapter
import com.example.songme.utils.Constants.FLAG_LOCAL
import kotlinx.android.synthetic.main.fragment_page_tracks.*
import java.util.ArrayList

class PageTracksFragment : Fragment() {

    private var tracks: List<Track>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tracks = arguments?.getParcelableArrayList(BUNDLE_TRACKS)
        return inflater.inflate(R.layout.fragment_page_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerPlayTracks.adapter = TrackAdapter(FLAG_LOCAL,
            { item, position ->
                activity?.let { it.startService(MusicService.getIntent(it, item, position)) }
            }).apply { tracks?.let { updateData(it) } }
    }

    companion object {
        private const val BUNDLE_TRACKS = "BUNDLE_TRACKS"
        fun newInstance(tracks: List<Track>) = PageTracksFragment().apply {
            arguments = bundleOf(BUNDLE_TRACKS to tracks)
        }
    }
}
