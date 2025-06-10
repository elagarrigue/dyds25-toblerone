package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie

interface MoviesRemoteSource {
    suspend fun getTMDBPopularMovies(): List<Movie>
    suspend fun getTMDBMovieDetails(id: Int): Movie
}