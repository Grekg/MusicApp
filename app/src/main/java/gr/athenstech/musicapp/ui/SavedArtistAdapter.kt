package gr.athenstech.musicapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import gr.athenstech.musicapp.ArtistEntity
import gr.athenstech.musicapp.R

class SavedArtistAdapter(
    private var artists: List<ArtistEntity>,
    private val onArtistClick: (String) -> Unit,
    private val onDeleteClick: (ArtistEntity) -> Unit
) : RecyclerView.Adapter<SavedArtistAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val artistNameTextView: TextView = view.findViewById(R.id.artist_name)
        val deleteButton: MaterialButton = view.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_artist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = artists[position]
        holder.artistNameTextView.text = artist.artistName
        
        holder.itemView.setOnClickListener {
            onArtistClick(artist.artistName)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(artist)
        }
    }

    override fun getItemCount(): Int = artists.size

    fun updateData(newArtists: List<ArtistEntity>) {
        artists = newArtists
        notifyDataSetChanged()
    }
}