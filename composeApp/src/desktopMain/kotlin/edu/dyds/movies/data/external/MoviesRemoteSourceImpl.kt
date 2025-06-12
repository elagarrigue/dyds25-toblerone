package edu.dyds.movies.data.external

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import edu.dyds.movies.domain.entity.Movie

class MoviesRemoteSourceImpl(private val tmdbHttpClient: HttpClient) : MoviesRemoteSource {

    override suspend fun getTMDBPopularMovies(): List<Movie> {
        val remoteResult: RemoteResult = tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()
        return remoteResult.results.map { it.toDomainMovie() }
    }

    override suspend fun getTMDBMovieDetails(id: Int): Movie {
        val remoteMovie: RemoteMovie = tmdbHttpClient.get("/3/movie/$id").body()
        return remoteMovie.toDomainMovie()
    }
}