package gr.athenstech.musicapp

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import gr.athenstech.musicapp.network.RetrofitClient
import gr.athenstech.musicapp.ui.Artist
import gr.athenstech.musicapp.ui.ArtistAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArtistAdapter
    private val artists = mutableListOf<Artist>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recycler_top_artists)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ArtistAdapter(artists)
        recyclerView.adapter = adapter

        val searchInput = findViewById<TextInputEditText>(R.id.searchInput)
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchInput.text.toString()
                if (query.isNotEmpty()) {
                    lifecycleScope.launch {
                        try {
                            val response = RetrofitClient.apiService.searchArtists(query)
                            val artistList = response.results?.artistmatches?.artist?.mapNotNull { artistItem ->
                                if (!artistItem.name.isNullOrEmpty()) {
                                    Artist(name = artistItem.name!!, imageUrl = null)
                                } else {
                                    null
                                }
                            } ?: emptyList()

                            artists.clear()
                            artists.addAll(artistList)
                            adapter.notifyDataSetChanged()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(this@MainActivity, "Error searching artists", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getTopArtists()
                val artistList = response.artists?.artist?.mapNotNull { artistItem ->
                    if (!artistItem.name.isNullOrEmpty()) {
                        Artist(name = artistItem.name!!, imageUrl = null)
                    } else {
                        null
                    }
                } ?: emptyList()

                artists.clear()
                artists.addAll(artistList)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
