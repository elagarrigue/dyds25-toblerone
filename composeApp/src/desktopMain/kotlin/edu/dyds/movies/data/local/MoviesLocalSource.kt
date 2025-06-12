package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

interface MoviesLocalSource {
    fun addAll(movies: List<Movie>)
    fun isEmpty(): Boolean
    fun getCacheMovies(): List<Movie>
}