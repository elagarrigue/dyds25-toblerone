package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.domain.entity.Movie

interface LocalMovies {
    fun addAll(movies: List<RemoteMovie>)
    fun isEmpty(): Boolean
    fun getCacheMovies(): List<Movie>
}