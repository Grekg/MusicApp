package gr.athenstech.musicapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_artists")
data class ArtistEntity(
    @PrimaryKey val artistName: String,
    val imageUrl: String
)