package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMovie

interface LocalMoviesInterface {
    fun addAll(movies: List<RemoteMovie>)
    fun isEmpty(): Boolean
    fun getCacheMovies(): List<RemoteMovie>
}