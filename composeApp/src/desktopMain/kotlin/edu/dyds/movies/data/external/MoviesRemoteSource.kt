package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie

interface PopularMoviesRemoteSource {
    suspend fun getPopularMovies(): List<Movie>
}

interface MovieByTitleRemoteSource {
    suspend fun getMovieByTitle(title: String): Movie
}