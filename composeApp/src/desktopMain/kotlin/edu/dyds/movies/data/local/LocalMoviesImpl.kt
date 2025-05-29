package edu.dyds.movies.data.local

import edu.dyds.movies.data.LocalMovies
import edu.dyds.movies.data.external.RemoteMovie

class LocalMoviesImpl : LocalMovies{
     private val cacheMovies: MutableList<RemoteMovie> = mutableListOf()

    override fun addAll(movies: List<RemoteMovie>) {
        cacheMovies.addAll(movies)
    }
    override fun isEmpty(): Boolean {
        return cacheMovies.isEmpty()
    }
    override fun getCacheMovies(): List<RemoteMovie> {
        return cacheMovies
    }
}