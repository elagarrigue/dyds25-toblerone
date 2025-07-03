package edu.dyds.movies.data.external.omdb

import edu.dyds.movies.data.external.MovieByTitleRemoteSource
import edu.dyds.movies.data.external.OMDBRemoteMovie

import edu.dyds.movies.domain.entity.Movie
import io.ktor.client .*
import io.ktor.client.call .*
import io.ktor.client.request .*

class OMDBMoviesExternalSource(
    private val omdbHttpClient: HttpClient,
) : MovieByTitleRemoteSource {

    override suspend fun getMovieByTitle(title: String): Movie =
        getOMDBMovieDetails(title).fromOMDBtoDomainMovie()

    private suspend fun getOMDBMovieDetails(title: String): OMDBRemoteMovie =
        omdbHttpClient.get("/?t=$title").body()
}