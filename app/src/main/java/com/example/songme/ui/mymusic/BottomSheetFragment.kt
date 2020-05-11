package com.example.songme.ui.mymusic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.songme.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment private constructor() : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_bottom_sheet, container, false)

    companion object {
        fun newInstance() = BottomSheetFragment()
    }
}
