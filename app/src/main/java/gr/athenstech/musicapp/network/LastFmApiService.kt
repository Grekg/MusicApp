package gr.athenstech.musicapp.network

import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApiService {
    @GET(".")
    suspend fun getTopArtists(
        @Query("method") method: String = "chart.gettopartists"
    ): Any
}

