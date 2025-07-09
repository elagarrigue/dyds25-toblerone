package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.data.external.MovieByTitleRemoteSource
import edu.dyds.movies.data.external.PopularMoviesRemoteSource
import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TMDBMoviesExternalSource(private val tmdbHttpClient: HttpClient) : PopularMoviesRemoteSource,
    MovieByTitleRemoteSource {

    override suspend fun getPopularMovies(): List<Movie> {
        return getTMDBMovies().results.map { it.fromTMDBtoDomainMovie() }
    }

    override suspend fun getMovieByTitle(title: String): Movie =
        getTMDBMovieDetails(title).apply { println(this) }.results.first().fromTMDBtoDomainMovie()

    private suspend fun getTMDBMovies(): TMDBRemoteResult {
        return tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()
    }

    private suspend fun getTMDBMovieDetails(title: String): TMDBRemoteResult =
        tmdbHttpClient.get("/3/search/movie?query=$title").body()
}