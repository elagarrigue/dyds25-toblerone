package edu.dyds.movies.data

import edu.dyds.movies.data.external.MoviesRemoteSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.data.external.RemoteResult
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.repository.MoviesRepository


class MoviesRepositoryImpl (private val moviesLocalSource: MoviesLocalSource, private val moviesRemoteSource: MoviesRemoteSource) : MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        if (moviesLocalSource.isEmpty()) {
            val remoteResult: RemoteResult = moviesRemoteSource.getTMDBPopularMovies()
            moviesLocalSource.addAll(remoteResult.results)
        }
            return moviesLocalSource.getCacheMovies()

    }

    override suspend fun getMovieDetails(id: Int): Movie? {
        return try{
            moviesRemoteSource.getTMDBMovieDetails(id).toDomainMovie()
        } catch (e: Exception) {
            null
        }
    }



}