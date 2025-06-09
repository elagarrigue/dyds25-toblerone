package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.domain.entity.Movie

interface MoviesLocalSource {
    fun addAll(movies: List<RemoteMovie>)
    fun isEmpty(): Boolean
    fun getCacheMovies(): List<Movie>
}