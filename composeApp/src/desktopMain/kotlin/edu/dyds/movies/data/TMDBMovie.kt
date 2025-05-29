package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.data.external.RemoteResult

interface TMDBMovie {
    suspend fun getTMDBPopularMovies(): RemoteResult
    suspend fun getTMDBMovieDetails(id: Int): RemoteMovie
}