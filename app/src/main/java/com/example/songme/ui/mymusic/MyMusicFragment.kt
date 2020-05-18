package com.example.songme.ui.mymusic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.songme.R
import com.example.songme.data.model.Track
import com.example.songme.data.repository.TrackRepository
import com.example.songme.data.source.local.TrackLocalDataSource
import com.example.songme.data.source.remote.TrackRemoteDataSource
import com.example.songme.service.MusicService
import com.example.songme.ui.adapter.TrackAdapter
import com.example.songme.ui.adapter.TrackAdapter.OnSendDataSelectedListener
import com.example.songme.utils.Constants.FLAG_LOCAL
import com.example.songme.utils.Constants.TAG_ACTION_BOTTOM
import com.example.songme.utils.showMessage
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_my_music.*

class MyMusicFragment private constructor() : BottomSheetDialogFragment(), MyMusicContract.View {

    private val presenter: MyMusicContract.Presenter by lazy {
        MyMusicPresenter(
            this,
            TrackRepository.getInstance(
                TrackLocalDataSource.getInstance(activity!!.contentResolver),
                TrackRemoteDataSource.getInstance()
            )
        )
    }
    private lateinit var callback: OnSendDataSelectedListener

    fun setOnSendDataSelectedListener(callback: OnSendDataSelectedListener) {
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_my_music, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getLocalTracks()
    }

    override fun showTracks(tracks: List<Track>) {
        recyclerLocalTrack.adapter = TrackAdapter(FLAG_LOCAL, { item, position ->
            activity?.let { it.startService(MusicService.getIntent(it, item, position)) }
            callback.sendData(item, position)
        }, { item, position ->
            showActionTrack()
        }).apply { updateData(tracks) }
    }

    override fun showError(exception: Exception) {
        context?.showMessage(exception.message.toString())
    }

    override fun showActionTrack() {
        fragmentManager?.let { BottomSheetFragment.newInstance().show(it, TAG_ACTION_BOTTOM) }
    }

    companion object {
        fun newInstance() = MyMusicFragment()
    }
}
