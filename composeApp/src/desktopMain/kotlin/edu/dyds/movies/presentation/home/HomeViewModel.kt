package edu.dyds.movies.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.usecase.PopularMoviesUseCase
import edu.dyds.movies.domain.entity.QualifiedMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch



class HomeViewModel (private val getPopularMoviesUseCase: PopularMoviesUseCase): ViewModel(){
    private val moviesStateMutableStateFlow = MutableStateFlow(MoviesUiState())
    val moviesStateFlow: Flow<MoviesUiState> = moviesStateMutableStateFlow

    fun getPopularMovies() {
        viewModelScope.launch {
            moviesStateMutableStateFlow.emit(
                MoviesUiState(isLoading = true)
            )
            moviesStateMutableStateFlow.emit(
                MoviesUiState(
                    isLoading = false,
                    movies = getPopularMoviesUseCase()
                )
            )
        }
    }

    data class MoviesUiState(
        val isLoading: Boolean = false,
        val movies: List<QualifiedMovie> = emptyList(),
    )

}

