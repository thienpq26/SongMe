package com.example.songme.ui.main

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.songme.R
import com.example.songme.data.model.Track
import com.example.songme.service.ServiceContract
import com.example.songme.service.MusicService
import com.example.songme.ui.genres.GenresFragment
import com.example.songme.ui.home.ActionBarFragment
import com.example.songme.ui.home.HomeFragment
import com.example.songme.ui.mymusic.MyMusicFragment
import com.example.songme.ui.adapter.TrackAdapter.OnSendDataSelectedListener
import com.example.songme.utils.Constants.STORAGE_PERMISSION_CODE
import com.example.songme.utils.Constants.TIME_FORMAT
import com.example.songme.utils.Constants.TIME_UPDATE_TRACK
import com.example.songme.utils.showMessage
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.play_music_screen.*
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    OnSendDataSelectedListener,
    ServiceContract.OnMediaPlayChange,
    View.OnClickListener {

    private var musicService: MusicService? = null
    private var isBound = false
    private var sheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null
    private var handler: Handler? = null
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.TrackBinder
            musicService = binder.getService()
            musicService?.setOnMediaChangeListener(this@MainActivity)
            updateSeekBar()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        if (!isBound) {
            bindService(MusicService.getIntent(this), serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun initComponents() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestStoragePermission()
        }
        addFragment(ActionBarFragment.newInstance())
        replaceFragment(HomeFragment.newInstance())
        sheetBehavior = BottomSheetBehavior.from(bottomSheetPlaymusic)
        handler = Handler()
    }

    private fun initListeners() {
        bottomNav?.setOnNavigationItemSelectedListener(this)
        bottomNav.menu.findItem(R.id.navigation_home).isChecked = true
        layoutPlay.setOnClickListener(this)
        buttonArrowDown.setOnClickListener(this)
        sheetBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                layoutContentMain.isVisible = newState != BottomSheetBehavior.STATE_EXPANDED
            }
        })
    }

    override fun onAttachFragment(fragment: Fragment) {
        when (fragment) {
            is HomeFragment -> fragment.setOnSendDataSelectedListener(this)
            is MyMusicFragment -> fragment.setOnSendDataSelectedListener(this)
        }
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

    override fun sendData(tracks: List<Track>, position: Int) {
        sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onTrackChange(track: Track?) {
        track?.also {
            textTrackTitlePlay.text = it.title
            textSingerPlay.text = it.author
            textNamePlaySmall.text = it.title
            textSingerPlaySmall.text = it.author
            Glide.with(this)
                .load(it.imageUrl)
                .circleCrop()
                .error(R.drawable.ic_music_black)
                .into(imagePlaySmall)
        }
    }

    override fun onMediaStateChange(isPlaying: Boolean) {
        onPlaySateChanged(isPlaying)
    }

    private fun onPlaySateChanged(isPlaying: Boolean) {
        if (isPlaying) {
            buttonPlayCircle.setImageResource(R.drawable.ic_pause_circle)
        } else {
            buttonPlayCircle.setImageResource(R.drawable.ic_play_circle_white)
        }
    }

    private fun updateSeekBar() {
        handler?.postDelayed(object : Runnable {
            override fun run() {
                seekBarTime.max = musicService?.getDuration() ?: 0
                textTotalTime.text =
                    SimpleDateFormat(TIME_FORMAT).format(musicService?.getDuration())
                textCurrentTime.text =
                    SimpleDateFormat(TIME_FORMAT).format(musicService?.getCurrentDuration())
                seekBarTime.progress = musicService?.getCurrentDuration() ?: 0
                handler?.postDelayed(this, TIME_UPDATE_TRACK.toLong())
            }
        }, TIME_UPDATE_TRACK.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            musicService?.setOnMediaChangeListener(null)
            unbindService(serviceConnection)
            isBound = false
        }
    }

    override fun onClick(v: View?) {
        musicService?.apply {
            when (v?.id) {
                R.id.layoutPlay -> sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                R.id.buttonArrowDown -> sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }
}
