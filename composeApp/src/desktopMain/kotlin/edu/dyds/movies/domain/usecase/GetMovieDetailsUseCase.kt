package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.entity.Movie

interface MovieDetailsUseCase {
    suspend operator fun invoke(id: Int): Movie?
}

class GetMovieDetailsUseCase ( private val moviesRepository: MoviesRepository) : MovieDetailsUseCase {
    override suspend operator fun invoke(id: Int): Movie? { return moviesRepository.getMovieDetails(id) }
}
