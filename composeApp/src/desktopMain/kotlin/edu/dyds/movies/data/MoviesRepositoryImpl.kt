package edu.dyds.movies.data

import edu.dyds.movies.data.external.Broker
import edu.dyds.movies.data.external.MovieByTitleRemoteSource
import edu.dyds.movies.data.external.PopularMoviesRemoteSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.repository.MoviesRepository


class MoviesRepositoryImpl (private val moviesLocalSource: MoviesLocalSource, private val moviesBroker: Broker) : MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        return try {
            if (moviesLocalSource.isEmpty()) {
                val domainMovies = moviesBroker.getPopularMovies()
                moviesLocalSource.addAll(domainMovies)
            }
            moviesLocalSource.getCacheMovies()
        } catch (e: Exception) {
            println("Error fetching popular movies: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getMovieDetails(title: String): Movie? {
        return try {
            moviesBroker.getMovieByTitle(title)
        } catch (e: Exception) {
            println("Error fetching movie details for title $title: ${e.message}")
            null
        }
    }


}