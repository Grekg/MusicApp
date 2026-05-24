package gr.athenstech.musicapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gr.athenstech.musicapp.ui.SavedArtistAdapter
import kotlinx.coroutines.launch

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

        adapter = SavedArtistAdapter(emptyList()) { artist ->
            lifecycleScope.launch {
                artistDao.delete(artist)
            }
        }
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            artistDao.getAllArtists().collect { artists ->
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
}