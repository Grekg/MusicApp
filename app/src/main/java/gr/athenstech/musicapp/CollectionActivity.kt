package gr.athenstech.musicapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import gr.athenstech.musicapp.ui.ArtistDetailActivity
import gr.athenstech.musicapp.ui.SavedArtistAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CollectionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedArtistAdapter
    private lateinit var emptyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        val database = AppDatabase.getDatabase(this)
        val artistDao = database.artistDao()

        recyclerView = findViewById(R.id.recycler_collection)
        emptyTextView = findViewById(R.id.empty_text)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = SavedArtistAdapter(
            emptyList(),
            onArtistClick = { artistName ->
                val intent = Intent(this, ArtistDetailActivity::class.java)
                intent.putExtra("ARTIST_NAME", artistName)
                startActivity(intent)
            }
        ) { artist ->
            lifecycleScope.launch(Dispatchers.IO) {
                artistDao.delete(artist)
            }
        }
        recyclerView.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {
            artistDao.getAllArtists().collect { artists ->
                withContext(Dispatchers.Main) {
                    adapter.updateData(artists)
                    if (artists.isEmpty()) {
                        emptyTextView.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        emptyTextView.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }
                }
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_favorites
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    false
                }
                R.id.nav_favorites -> true
                else -> false
            }
        }
    }
}