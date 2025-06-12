package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

class MoviesLocalSourceImpl : MoviesLocalSource {
    private val cacheMovies: MutableList<Movie> = mutableListOf()

    override fun addAll(movies: List<Movie>) {
        cacheMovies.addAll(movies)
    }

    override fun isEmpty(): Boolean {
        return cacheMovies.isEmpty()
    }

    override fun getCacheMovies(): List<Movie> {
        return cacheMovies
    }
}