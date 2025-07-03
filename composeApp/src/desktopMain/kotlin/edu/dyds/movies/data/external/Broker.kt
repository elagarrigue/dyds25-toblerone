package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie


class Broker(private val tmdbPopularSource: PopularMoviesRemoteSource, private val tmdbDetailSource: MovieByTitleRemoteSource, private val omdbSource: MovieByTitleRemoteSource) :
    PopularMoviesRemoteSource, MovieByTitleRemoteSource {

    override suspend fun getPopularMovies(): List<Movie> {
        return tmdbPopularSource.getPopularMovies()
    }
    override suspend fun getMovieByTitle(title: String): Movie {
        val tmdbMovie = tmdbDetailSource.getMovieByTitle(title)
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