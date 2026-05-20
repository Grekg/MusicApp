package gr.athenstech.musicapp.network

import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApiService {
    @GET(".")
    suspend fun getTopArtists(
        @Query("method") method: String = "chart.gettopartists"
    ): ChartResponse

    @GET(".")
    suspend fun searchArtists(
        @Query("artist") artist: String,
        @Query("method") method: String = "artist.search"
    ): SearchResponse
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

data class SearchResponse(
    val results: SearchResultsWrapper?
)

data class SearchResultsWrapper(
    val artistmatches: ArtistMatches?
)

data class ArtistMatches(
    val artist: List<ArtistItem>?
)

