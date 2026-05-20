package gr.athenstech.musicapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gr.athenstech.musicapp.R

data class Artist(val name: String, val imageUrl: String?)

class ArtistAdapter(
    private var artists: List<Artist>,
    private val onArtistClick: (Artist) -> Unit
) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textArtistName: TextView = itemView.findViewById(R.id.text_artist_name)
        val imageArtist: ImageView = itemView.findViewById(R.id.image_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist_card, parent, false)
        return ArtistViewHolder(view)
    }

    override fun getItemCount(): Int = artists.size

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artists[position]
        holder.textArtistName.text = artist.name
        holder.itemView.setOnClickListener {
            onArtistClick(artist)
        }
    }
}

