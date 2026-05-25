package gr.athenstech.musicapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import gr.athenstech.musicapp.AppDatabase
import gr.athenstech.musicapp.ArtistEntity
import gr.athenstech.musicapp.R
import gr.athenstech.musicapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistDetailActivity : AppCompatActivity() {
    private lateinit var recyclerTopTracks: RecyclerView
    private lateinit var recyclerSimilarArtists: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var artistAdapter: ArtistAdapter
    private val tracks = mutableListOf<Track>()
    private val similarArtists = mutableListOf<Artist>()
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_artist_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.detail_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val artistName = intent.getStringExtra("ARTIST_NAME") ?: "Unknown Artist"
        val detailTextName = findViewById<TextView>(R.id.detail_text_name)
        detailTextName.text = artistName

        val saveButton = findViewById<MaterialButton>(R.id.action_save)
        checkFavoriteStatus(artistName, saveButton)

        saveButton.setOnClickListener {
            toggleFavorite(artistName, saveButton)
        }

        val textArtistBio = findViewById<TextView>(R.id.text_artist_bio)
        val buttonReadMore = findViewById<MaterialButton>(R.id.button_read_more)

        val shareButton = findViewById<MaterialButton>(R.id.button_share)
        shareButton.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out $artistName on MusicApp!")
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        recyclerTopTracks = findViewById(R.id.recycler_top_tracks)
        recyclerTopTracks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackAdapter = TrackAdapter(tracks)
        recyclerTopTracks.adapter = trackAdapter

        recyclerSimilarArtists = findViewById(R.id.recycler_similar_artists)
        recyclerSimilarArtists.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        artistAdapter = ArtistAdapter(similarArtists) { artist ->
            val intent = Intent(this, ArtistDetailActivity::class.java)
            intent.putExtra("ARTIST_NAME", artist.name)
            startActivity(intent)
        }
        recyclerSimilarArtists.adapter = artistAdapter

        lifecycleScope.launch {
            try {
                val infoResponse = RetrofitClient.apiService.getArtistInfo(artistName)
                val bioSummary = infoResponse.artist?.bio?.summary ?: ""
                textArtistBio.text = HtmlCompat.fromHtml(bioSummary, HtmlCompat.FROM_HTML_MODE_LEGACY)
                
                val artistUrl = infoResponse.artist?.url
                buttonReadMore.setOnClickListener {
                    if (!artistUrl.isNullOrEmpty()) {
                        val browserIntent = Intent(Intent.ACTION_VIEW, artistUrl.toUri())
                        startActivity(browserIntent)
                    }
                }

                val tracksResponse = RetrofitClient.apiService.getArtistTopTracks(artistName)
                val trackList = tracksResponse.toptracks?.track?.mapNotNull { trackItem ->
                    if (!trackItem.name.isNullOrEmpty()) {
                        Track(name = trackItem.name, playcount = trackItem.playcount ?: "0")
                    } else {
                        null
                    }
                }?.take(5) ?: emptyList()

                tracks.clear()
                tracks.addAll(trackList)
                trackAdapter.notifyDataSetChanged()

                val artistsResponse = RetrofitClient.apiService.getSimilarArtists(artistName)
                val artistList = artistsResponse.similarartists?.artist?.mapNotNull { artistItem ->
                    if (!artistItem.name.isNullOrEmpty()) {
                        Artist(name = artistItem.name, imageUrl = null)
                    } else {
                        null
                    }
                } ?: emptyList()

                similarArtists.clear()
                similarArtists.addAll(artistList)
                artistAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@ArtistDetailActivity, "Error loading artist details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkFavoriteStatus(artistName: String, button: MaterialButton) {
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(this@ArtistDetailActivity).artistDao()
            isFavorite = dao.isArtistSaved(artistName)
            withContext(Dispatchers.Main) {
                updateSaveButtonIcon(button)
            }
        }
    }

    private fun toggleFavorite(artistName: String, button: MaterialButton) {
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(this@ArtistDetailActivity).artistDao()
            if (isFavorite) {
                dao.deleteByName(artistName)
                isFavorite = false
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ArtistDetailActivity, "Removed from Favorites", Toast.LENGTH_SHORT).show()
                }
            } else {
                dao.insert(ArtistEntity(artistName, ""))
                isFavorite = true
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ArtistDetailActivity, "Added to Favorites", Toast.LENGTH_SHORT).show()
                }
            }
            withContext(Dispatchers.Main) {
                updateSaveButtonIcon(button)
            }
        }
    }

    private fun updateSaveButtonIcon(button: MaterialButton) {
        if (isFavorite) {
            button.setIconResource(android.R.drawable.btn_star_big_on)
        } else {
            button.setIconResource(android.R.drawable.btn_star_big_off)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}