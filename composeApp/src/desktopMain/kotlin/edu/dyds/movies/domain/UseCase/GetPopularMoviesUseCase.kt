package edu.dyds.movies.domain.UseCase

import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

private const val MIN_VOTE_AVERAGE = 6.0

interface PopularMoviesUseCase {
    suspend operator fun invoke(): List<QualifiedMovie>
}

class GetPopularMoviesUseCase(private val moviesRepository: MoviesRepository) : PopularMoviesUseCase {
    override suspend operator fun invoke(): List<QualifiedMovie> {
        return moviesRepository.getPopularMovies().sortedByDescending { it.voteAverage }.
            map { movie ->
                QualifiedMovie(
                    movie = movie,
                    isGoodMovie = movie.voteAverage >= MIN_VOTE_AVERAGE
                )
            }
    }

}