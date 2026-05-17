package gr.athenstech.musicapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artist: ArtistEntity)

    @Delete
    suspend fun delete(artist: ArtistEntity)

    @Query("SELECT * FROM saved_artists")
    fun getAllArtists(): Flow<List<ArtistEntity>>
}