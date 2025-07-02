package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie

interface MoviesRemoteSource {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getMovieByTitle(title: String): Movie
}