package edu.dyds.movies.data

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.data.external.RemoteResult
import edu.dyds.movies.domain.repository.MoviesRepository


class MoviesRepositoryImpl (private val cachedMovies: LocalMoviesInterface,private val remoteManager: RemoteMoviesInterface) : MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        if (cachedMovies.isEmpty()) {
            val remoteResult: RemoteResult = remoteManager.getTMDBPopularMovies()
            cachedMovies.addAll(remoteResult.results)
        }
            return cachedMovies.getCacheMovies().map { it.toDomainMovie() }

    }

    override suspend fun getMovieDetails(id: Int): Movie? {
        return try{
            remoteManager.getTMDBMovieDetails(id).toDomainMovie()
        } catch (e: Exception) {
            null
        }
    }



}