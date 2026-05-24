package gr.athenstech.musicapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import gr.athenstech.musicapp.R
import gr.athenstech.musicapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ArtistDetailActivity : AppCompatActivity() {
    private lateinit var recyclerTopTracks: RecyclerView
    private lateinit var recyclerSimilarArtists: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var artistAdapter: ArtistAdapter
    private val tracks = mutableListOf<Track>()
    private val similarArtists = mutableListOf<Artist>()

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
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(artistUrl))
                        startActivity(browserIntent)
                    }
                }

                val tracksResponse = RetrofitClient.apiService.getArtistTopTracks(artistName)
                val trackList = tracksResponse.toptracks?.track?.mapNotNull { trackItem ->
                    if (!trackItem.name.isNullOrEmpty()) {
                        Track(name = trackItem.name!!, playcount = trackItem.playcount ?: "0")
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
                        Artist(name = artistItem.name!!, imageUrl = null)
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
