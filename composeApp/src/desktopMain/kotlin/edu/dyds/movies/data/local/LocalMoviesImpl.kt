package edu.dyds.movies.data.local

import edu.dyds.movies.data.LocalMovies
import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.domain.entity.Movie

class LocalMoviesImpl : LocalMovies{
     private val cacheMovies: MutableList<Movie> = mutableListOf()

    override fun addAll(movies: List<RemoteMovie>) {
        cacheMovies.addAll(movies.map { it.toDomainMovie() })
    }
    override fun isEmpty(): Boolean {
        return cacheMovies.isEmpty()
    }
    override fun getCacheMovies(): List<Movie> {
        return cacheMovies
    }
}