package edu.dyds.movies.data.external

import edu.dyds.movies.data.external.omdb.OMDBMoviesExternalSource
import edu.dyds.movies.data.external.tmdb.TMDBMoviesExternalSource
import edu.dyds.movies.domain.entity.Movie


class Broker(private val tmdbSource: TMDBMoviesExternalSource, private val omdbSource: OMDBMoviesExternalSource) :
    PopularMoviesRemoteSource, MovieByTitleRemoteSource {

    override suspend fun getPopularMovies(): List<Movie> {
        return tmdbSource.getPopularMovies()
    }
    override suspend fun getMovieByTitle(title: String): Movie {
        val tmdbMovie = tmdbSource.getMovieByTitle(title)
        val omdbMovie = omdbSource.getMovieByTitle(title)
        return buildMovie(tmdbMovie,omdbMovie)
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