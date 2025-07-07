package edu.dyds.movies.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.usecase.MovieDetailsUseCase
import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val getMovieDetailsUseCase: MovieDetailsUseCase) : ViewModel() {
    private val movieDetailStateMutableStateFlow = MutableStateFlow(MovieDetailUiState())
    val movieDetailStateFlow: Flow<MovieDetailUiState> = movieDetailStateMutableStateFlow

    fun getMovieDetails(title: String) {
        viewModelScope.launch {
            val movie = getMovieDetailsUseCase(title)
            movieDetailStateMutableStateFlow.emit(
                MovieDetailUiState(
                    isLoading = false,
                    movie = movie
                )
            )
        }
    }

    data class MovieDetailUiState(
        val isLoading: Boolean = true,
        val movie: Movie? = null,
    )
}