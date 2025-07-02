package edu.dyds.movies.data.external

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import edu.dyds.movies.domain.entity.Movie

class MoviesRemoteSourceImpl(private val tmdbHttpClient: HttpClient) : MoviesRemoteSource {

    override suspend fun getPopularMovies(): List<Movie>  =
        getTMDBMovies().results.map { it.toDomainMovie() }

    override suspend fun getMovieByTitle(title: String): Movie =
        getTMDBMovieDetails(title).apply { println(this) }.results.first().toDomainMovie()



    private suspend fun getTMDBMovies(): RemoteResult =
        tmdbHttpClient.get ( "/3/discover/movie?sort_by=popularity.desc" ).body()

    private suspend fun getTMDBMovieDetails(title: String): RemoteResult =
        tmdbHttpClient.get("/3/search/movie?query=$title").body()

}