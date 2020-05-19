package com.example.songme.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.songme.R
import com.example.songme.data.model.Track
import com.example.songme.utils.Constants.FLAG_LOCAL
import com.example.songme.utils.Constants.VIEW_TYPE_LOCAL
import com.example.songme.utils.Constants.VIEW_TYPE_REMOTE
import kotlinx.android.synthetic.main.horizontal_linear_item_track.view.*

class TrackAdapter(
    private val flag: Int,
    private val onItemClick: (List<Track>, Int) -> Unit = { _, _ -> },
    private val onActionClick: (Track, Int) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val tracks = mutableListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_LOCAL) {
            LocalViewHolder(
                inflater.inflate(R.layout.horizontal_linear_item_track, parent, false),
                onItemClick, onActionClick
            )
        } else {
            RemoteViewHolder(
                inflater.inflate(R.layout.vertical_linear_item_track, parent, false),
                onItemClick
            )
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun updateData(newTracks: List<Track>) {
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int =
        if (flag == FLAG_LOCAL) VIEW_TYPE_LOCAL else VIEW_TYPE_REMOTE

    class RemoteViewHolder(
        itemView: View,
        private val onItemClick: (List<Track>, Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindData(tracks: List<Track>, position: Int) {
            itemView.run {
                Glide.with(itemView)
                    .load(tracks[position].imageUrl)
                    .error(R.drawable.ic_music_black)
                    .into(imageTrack)
                textTrackTitle.text = tracks[position].title
                textSinger.text = tracks[position].author
                setOnClickListener { onItemClick(tracks, adapterPosition) }
            }
        }
    }

    class LocalViewHolder(
        itemView: View,
        private val onItemClick: (List<Track>, Int) -> Unit,
        private val onActionClick: (Track, Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindData(tracks: List<Track>, position: Int) {
            itemView.run {
                Glide.with(itemView)
                    .load(tracks[position].imageUrl)
                    .error(R.drawable.ic_music_black)
                    .into(imageTrack)
                textTrackTitle.text = tracks[position].title
                textSinger.text = tracks[position].author
                setOnClickListener { onItemClick(tracks, adapterPosition) }
                buttonMore.setOnClickListener {
                    onActionClick(tracks[position], adapterPosition)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LocalViewHolder -> holder.bindData(tracks, position)
            is RemoteViewHolder -> holder.bindData(tracks, position)
        }
    }

    interface OnSendDataSelectedListener {
        fun sendData(tracks: List<Track>, position: Int)
        fun sendAction()
    }
}
