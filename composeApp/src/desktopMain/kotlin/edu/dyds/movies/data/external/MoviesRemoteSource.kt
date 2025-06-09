package edu.dyds.movies.data.external

interface MoviesRemoteSource {
    suspend fun getTMDBPopularMovies(): RemoteResult
    suspend fun getTMDBMovieDetails(id: Int): RemoteMovie
}