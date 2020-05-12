package com.example.songme.ui.main

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.songme.R
import com.example.songme.ui.genres.GenresFragment
import com.example.songme.ui.home.ActionBarFragment
import com.example.songme.ui.home.HomeFragment
import com.example.songme.ui.mymusic.MyMusicFragment
import com.example.songme.utils.Constants.STORAGE_PERMISSION_CODE
import com.example.songme.utils.showMessage
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestStoragePermission()
        }
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

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)) {
            AlertDialog.Builder(this)
                .setTitle(R.string.title_permission)
                .setMessage(R.string.message_permission)
                .setPositiveButton(R.string.title_open_dialog) { _, _ ->
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE
                    )
                }
                .setNegativeButton(R.string.title_cancel_dialog) { dialog, _ -> dialog.dismiss() }
                .create().show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showMessage("${R.string.title_permission_denied}")
            }
        }
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
