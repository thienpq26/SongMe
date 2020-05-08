package com.example.songme.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.songme.R
import com.example.songme.ui.genres.GenresFragment
import com.example.songme.ui.home.ActionBarFragment
import com.example.songme.ui.home.HomeFragment
import com.example.songme.ui.mymusic.MyMusicFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initListeners()
    }

    private fun initViews() {
        addFragment(ActionBarFragment.newInstance())
        replaceFragment(HomeFragment.newInstance())
    }

    private fun initListeners() {
        bottomNav?.setOnNavigationItemSelectedListener(this)
        bottomNav.menu.findItem(R.id.navigation_home).isChecked = true
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.frameActionBar, fragment)
            .commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContent, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_mymusic -> MyMusicFragment.newInstance()
            R.id.navigation_home -> HomeFragment.newInstance()
            R.id.navigation_genres -> GenresFragment.newInstance()
            else -> null
        }?.also { replaceFragment(it) }
        return true
    }
}
