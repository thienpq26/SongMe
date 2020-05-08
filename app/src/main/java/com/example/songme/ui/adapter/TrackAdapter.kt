package com.example.songme.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.songme.R
import com.example.songme.data.model.Track
import kotlinx.android.synthetic.main.item_track.view.*

class TrackAdapter(private val onItemClick: (Track, Int) -> Unit = { _, _ -> }) :
    RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    private val tracks = mutableListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false),
            onItemClick
        )

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(tracks[position])
    }

    fun updateData(newTracks: List<Track>) {
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, private val onItemClick: (Track, Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        fun bindData(track: Track) {
            itemView.run {
                Glide.with(itemView)
                    .load(track.imageUrl)
                    .error(R.drawable.ic_music_black)
                    .into(imageTrack)
                titleTrack.text = track.title
                titleSinger.text = track.author
                itemView.setOnClickListener { onItemClick(track, adapterPosition) }
            }
        }
    }
}
