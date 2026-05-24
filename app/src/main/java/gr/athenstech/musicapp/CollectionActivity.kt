package gr.athenstech.musicapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gr.athenstech.musicapp.ui.SavedArtistAdapter
import kotlinx.coroutines.launch

class CollectionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedArtistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        recyclerView = findViewById(R.id.recycler_collection)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = SavedArtistAdapter(emptyList()) { }
        recyclerView.adapter = adapter

        val database = AppDatabase.getDatabase(this)
        val artistDao = database.artistDao()

        lifecycleScope.launch {
            artistDao.getAllArtists().collect { artists ->
                adapter.updateData(artists)
            }
        }
    }
}