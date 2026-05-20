package gr.athenstech.musicapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gr.athenstech.musicapp.R

data class Track(val name: String, val playcount: String)

class TrackAdapter(private var tracks: List<Track>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTrackNumber: TextView = itemView.findViewById(R.id.text_track_number)
        val textTrackName: TextView = itemView.findViewById(R.id.text_track_name)
        val textTrackPlaycount: TextView = itemView.findViewById(R.id.text_track_playcount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.textTrackNumber.text = (position + 1).toString()
        holder.textTrackName.text = track.name
        holder.textTrackPlaycount.text = track.playcount
    }
}

