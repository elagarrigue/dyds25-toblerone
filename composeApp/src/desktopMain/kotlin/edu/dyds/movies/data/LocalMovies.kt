package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMovie

interface LocalMovies {
    fun addAll(movies: List<RemoteMovie>)
    fun isEmpty(): Boolean
    fun getCacheMovies(): List<RemoteMovie>
}