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

    @GET(".")
    suspend fun getArtistTopTracks(
        @Query("artist") artist: String,
        @Query("method") method: String = "artist.gettoptracks"
    ): TopTracksResponse

    @GET(".")
    suspend fun getSimilarArtists(
        @Query("artist") artist: String,
        @Query("method") method: String = "artist.getsimilar"
    ): SimilarArtistsResponse

    @GET(".")
    suspend fun getArtistInfo(
        @Query("artist") artist: String,
        @Query("method") method: String = "artist.getinfo"
    ): ArtistInfoResponse
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

data class TopTracksResponse(
    val toptracks: TopTracksWrapper?
)

data class TopTracksWrapper(
    val track: List<TrackItem>?
)

data class TrackItem(
    val name: String?,
    val playcount: String?
)

data class SimilarArtistsResponse(
    val similarartists: SimilarArtistsWrapper?
)

data class SimilarArtistsWrapper(
    val artist: List<ArtistItem>?
)

data class ArtistInfoResponse(
    val artist: ArtistDetail?
)

data class ArtistDetail(
    val name: String?,
    val url: String?,
    val bio: ArtistBio?
)

data class ArtistBio(
    val summary: String?
)
