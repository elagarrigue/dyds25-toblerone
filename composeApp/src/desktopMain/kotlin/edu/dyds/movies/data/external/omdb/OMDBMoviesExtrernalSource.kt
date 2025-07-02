package edu.dyds.movies.data.external.omdb

import edu.dyds.movies.data.external.MovieByTitleRemoteSource
import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.domain.entity.Movie
import io.ktor.client .*
import io.ktor.client.call .*
import io.ktor.client.request .*

class OMDBMoviesExternalSource(
    private val omdbHttpClient: HttpClient,
) : MovieByTitleRemoteSource {

    override suspend fun getMovieByTitle(title: String): Movie =
        getOMDBMovieDetails(title).toDomainMovie()

    private suspend fun getOMDBMovieDetails(title: String): RemoteMovie =
        omdbHttpClient.get("/?t=$title").body()
}