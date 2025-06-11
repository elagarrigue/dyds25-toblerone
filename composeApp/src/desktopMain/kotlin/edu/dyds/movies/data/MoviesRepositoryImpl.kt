package edu.dyds.movies.data

import edu.dyds.movies.data.external.MoviesRemoteSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.repository.MoviesRepository


class MoviesRepositoryImpl (private val moviesLocalSource: MoviesLocalSource, private val moviesRemoteSource: MoviesRemoteSource) : MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        return try {
            if (moviesLocalSource.isEmpty()) {
                val domainMovies = moviesRemoteSource.getTMDBPopularMovies()
                moviesLocalSource.addAll(domainMovies)
            }
            moviesLocalSource.getCacheMovies()
        } catch (e: Exception) {
            println("Error fetching popular movies: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getMovieDetails(id: Int): Movie? {
        return try {
            moviesRemoteSource.getTMDBMovieDetails(id)
        } catch (e: Exception) {
            println("Error fetching movie details for id $id: ${e.message}")
            null
        }
    }


}