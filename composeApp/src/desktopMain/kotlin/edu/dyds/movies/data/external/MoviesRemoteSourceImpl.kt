package edu.dyds.movies.data.external

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class MoviesRemoteSourceImpl(private val tmdbHttpClient: HttpClient) : MoviesRemoteSource {

    override suspend fun getTMDBPopularMovies(): RemoteResult =
        tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()

    override suspend fun getTMDBMovieDetails(id: Int): RemoteMovie =
        tmdbHttpClient.get("/3/movie/$id").body()
}