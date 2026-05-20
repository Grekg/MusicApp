package gr.athenstech.musicapp

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import gr.athenstech.musicapp.network.NetworkUtils
import gr.athenstech.musicapp.network.RetrofitClient
import gr.athenstech.musicapp.ui.Artist
import gr.athenstech.musicapp.ui.ArtistAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArtistAdapter
    private lateinit var layoutLoading: View
    private lateinit var layoutError: View
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

        layoutLoading = findViewById(R.id.layoutLoading)
        layoutError = findViewById(R.id.layoutError)
        val retryButton = findViewById<Button>(R.id.retryButton)

        retryButton.setOnClickListener {
            checkInternet()
        }

        checkInternet()

        val searchInput = findViewById<TextInputEditText>(R.id.searchInput)
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchInput.text.toString()
                if (query.isNotEmpty()) {
                    Toast.makeText(this, "Searching for: $query", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }
    }

    private fun fetchData() {
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
                layoutLoading.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
                layoutLoading.visibility = View.GONE
                layoutError.visibility = View.VISIBLE
            }
        }
    }

    private fun checkInternet() {
        if (NetworkUtils.isInternetAvailable(this)) {
            layoutError.visibility = View.GONE
            layoutLoading.visibility = View.VISIBLE
            fetchData()
        } else {
            layoutLoading.visibility = View.GONE
            layoutError.visibility = View.VISIBLE
        }
    }
}
