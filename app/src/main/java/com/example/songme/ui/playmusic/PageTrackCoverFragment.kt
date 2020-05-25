package com.example.songme.ui.playmusic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.songme.utils.loadImage
import com.example.songme.R
import kotlinx.android.synthetic.main.fragment_page_track_cover.*

class PageTrackCoverFragment : Fragment() {

    private var imageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        imageUrl = arguments?.getString(BUNDLE_IMAGE)
        return inflater.inflate(R.layout.fragment_page_track_cover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageUrl?.let { imageTrackCover.loadImage(it, R.drawable.ic_music_black) }
    }

    companion object {
        private const val BUNDLE_IMAGE = "BUNDLE_IMAGE"
        fun newInstance(imageUrl: String) = PageTrackCoverFragment().apply {
            arguments = bundleOf(BUNDLE_IMAGE to imageUrl)
        }
    }
}
