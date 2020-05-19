package com.example.songme.ui.playmusic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.songme.R
import com.example.songme.ui.adapter.TrackAdapter.OnSendDataSelectedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet_download.*

class BottomSheetDownloadFragment private constructor() : BottomSheetDialogFragment() {

    private lateinit var callback: OnSendDataSelectedListener

    fun setOnSendDataSelectedListener(callback: OnSendDataSelectedListener) {
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_bottom_sheet_download, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutDownload.setOnClickListener {
            callback.sendAction()
        }
    }

    companion object {
        fun newInstance() = BottomSheetDownloadFragment()
    }
}
