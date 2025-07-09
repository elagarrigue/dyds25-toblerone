package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie


class BrokerExternalSource(
    private val tmdbDetailSource: MovieByTitleRemoteSource,
    private val omdbSource: MovieByTitleRemoteSource
) :
    MovieByTitleRemoteSource {

    override suspend fun getMovieByTitle(title: String): Movie? {
        val tmdbMovie = try {
            tmdbDetailSource.getMovieByTitle(title)
        } catch (e: Exception) {
            println("TMDB error: ${e.message}")
            null
        }
        val omdbMovie = try {
            omdbSource.getMovieByTitle(title)
        } catch (e: Exception) {
            println("OMDB error: ${e.message}")
            null
        }
        return when {
            tmdbMovie != null && omdbMovie != null -> buildMovie(tmdbMovie, omdbMovie)
            tmdbMovie != null -> tmdbMovie.copy(overview = "TMDB: ${tmdbMovie.overview}")
            omdbMovie != null -> omdbMovie.copy(overview = "OMDB: ${omdbMovie.overview}")
            else -> {
                throw IllegalArgumentException("No movie found for title: $title")
            }
        }
    }

    private fun buildMovie(
        tmdbMovie: Movie,
        omdbMovie: Movie
    ) =
        Movie(
            id = tmdbMovie.id,
            title = tmdbMovie.title,
            overview = "TMDB: ${tmdbMovie.overview}\n\n OMDB: ${omdbMovie.overview}",
            releaseDate = tmdbMovie.releaseDate,
            poster = tmdbMovie.poster,
            backdrop = tmdbMovie.backdrop,
            originalTitle = tmdbMovie.originalTitle,
            originalLanguage = tmdbMovie.originalLanguage,
            popularity = (tmdbMovie.popularity + omdbMovie.popularity) / 2.0,
            voteAverage = (tmdbMovie.voteAverage + omdbMovie.voteAverage) / 2.0
        )
}