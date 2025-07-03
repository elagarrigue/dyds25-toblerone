package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OMDBRemoteMovie(
    @SerialName("Title") val title: String? = null,
    @SerialName("Plot") val plot: String? = null,
    @SerialName("Released") val released: String? = null,
    @SerialName("Year") val year: String? = null,
    @SerialName("Poster") val poster: String? = null,
    @SerialName("Language") val language: String? = null,
    @SerialName("Metascore") val metaScore: String? = null,
    val imdbRating: String? = null,
    @SerialName("Response") val response: String? = null,
    @SerialName("Error") val error: String? = null
) {
    fun fromOMDBtoDomainMovie() = Movie(
        id = title?.hashCode() ?: 0,
        title = title ?: "Unknown Title",
        overview = plot ?: "No plot available",
        releaseDate = if (released?.isNotEmpty() == true && released != "N/A") released else (year ?: "Unknown"),
        poster = if (poster?.isNotEmpty() == true && poster != "N/A") poster else "",
        backdrop = if (poster?.isNotEmpty() == true && poster != "N/A") poster else "",
        originalTitle = title ?: "Unknown Title",
        originalLanguage = language ?: "Unknown",
        popularity = imdbRating?.toDoubleOrNull() ?: 0.0,
        voteAverage = if (metaScore?.isNotEmpty() == true && metaScore != "N/A") metaScore.toDouble() else 0.0
    )

}


@Serializable
data class TMDBRemoteMovie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("original_language") val originalLanguage: String,
    val popularity: Double? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,

    ) {
    fun fromTMDBtoDomainMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            overview = overview,
            releaseDate = releaseDate ?: "",
            poster = "https://image.tmdb.org/t/p/w185$posterPath",
            backdrop = backdropPath.let { "https://image.tmdb.org/t/p/w780$it" },
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            popularity = popularity ?: 0.0,
            voteAverage = voteAverage ?: 0.0
        )
    }
}