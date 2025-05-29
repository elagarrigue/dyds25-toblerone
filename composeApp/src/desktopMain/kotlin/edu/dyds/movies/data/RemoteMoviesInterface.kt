package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.data.external.RemoteResult
import io.ktor.client.call.body
import io.ktor.client.request.get

interface RemoteMoviesInterface {
    suspend fun getTMDBPopularMovies(): RemoteResult
    suspend fun getTMDBMovieDetails(id: Int): RemoteMovie
}