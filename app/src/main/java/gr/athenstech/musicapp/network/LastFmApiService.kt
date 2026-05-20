package gr.athenstech.musicapp.network

import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApiService {
    @GET(".")
    suspend fun getTopArtists(
        @Query("method") method: String = "chart.gettopartists"
    ): ChartResponse
}

data class ChartResponse(
    val artists: ArtistsResponse?
)

data class ArtistsResponse(
    val artist: List<ArtistItem>?
)

data class ArtistItem(
    val name: String?,
    val playcount: String?,
    val image: List<ImageItem>?
)

data class ImageItem(
    val size: String?,
    val text: String?
)

