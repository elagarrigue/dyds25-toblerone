package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.hashCode

@Serializable
data class OMDBRemoteMovie(
    @SerialName ("Title") val title: String,
    @SerialName("Plot") val plot: String,
    @SerialName( value = "Released") val released: String?,
    @SerialName( value = "Year") val year: String,
    @SerialName( value = "Poster") val poster: String,
    @SerialName( value = "Language") val language: String,
    @SerialName( value = "Metascore") val metaScore: String?,
    val imdbRating: Double?,

    ){
    fun fromOMDBtoDomainMovie() = Movie(
        id = title.hashCode(),
        title = title,
        overview = plot,
        releaseDate = if (released?.isNotEmpty() == true && released != "N/A") released else year,
        poster = poster,
        backdrop = poster,
        originalTitle = title,
        originalLanguage = language,
        popularity = imdbRating ?: 0.0,
        voteAverage = if (metaScore?.isNotEmpty() == true && metaScore != "N/A") metaScore.toDouble() else 0.0
    )
}


@Serializable
data class RemoteMovie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("original_language") val originalLanguage: String,
    val popularity: Double,
    @SerialName("vote_average") val voteAverage: Double,

    )

{
    fun toDomainMovie(): Movie {
        println(releaseDate)
        return Movie(
            id = id,
            title = title,
            overview = overview,
            releaseDate = releaseDate ,
            poster = "https://image.tmdb.org/t/p/w185$posterPath",
            backdrop = backdropPath.let { "https://image.tmdb.org/t/p/w780$it" },
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            popularity = popularity,
            voteAverage = voteAverage
        )
    }
}

