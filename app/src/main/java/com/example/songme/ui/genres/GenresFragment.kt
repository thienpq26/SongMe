package com.example.songme.ui.genres

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.songme.R
import com.example.songme.ui.adapter.PagerAdapter
import com.example.songme.utils.Genres.Epic
import com.example.songme.utils.Genres.HIP_HOP
import com.example.songme.utils.Genres.KPOP
import com.example.songme.utils.Genres.POP
import com.example.songme.utils.Genres.UK
import com.example.songme.utils.Genres.US
import com.example.songme.utils.Genres.VINAHOUSE
import com.example.songme.utils.Genres.VPOP
import kotlinx.android.synthetic.main.fragment_genres.*

class GenresFragment private constructor() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_genres, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerAdapter = fragmentManager?.let { PagerAdapter(it) }
        pagerAdapter?.apply {
            addFragment(GenreFragment.newInstance(VPOP), getString(R.string.title_tab_vpop))
            addFragment(GenreFragment.newInstance(KPOP), getString(R.string.title_tab_kpop))
            addFragment(GenreFragment.newInstance(US), getString(R.string.title_tab_us))
            addFragment(GenreFragment.newInstance(UK), getString(R.string.title_tab_uk))
            addFragment(GenreFragment.newInstance(HIP_HOP), getString(R.string.title_tab_hip_hop))
            addFragment(GenreFragment.newInstance(POP), getString(R.string.title_tab_pop))
            addFragment(GenreFragment.newInstance(Epic), getString(R.string.title_tab_epic))
            addFragment(
                GenreFragment.newInstance(VINAHOUSE),
                getString(R.string.title_tab_vinahouse)
            )
        }
        viewPagerGenres.adapter = pagerAdapter
        tabLayoutGenres.setupWithViewPager(viewPagerGenres)
    }

    companion object {
        fun newInstance() = GenresFragment()
    }
}
