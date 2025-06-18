package presentation.detail

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.MovieDetailsUseCase
import edu.dyds.movies.presentation.detail.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    val testDispatcher = UnconfinedTestDispatcher()
    private val getMovieDetailsUseCase = object : MovieDetailsUseCase {
        override suspend fun invoke(id: Int): Movie? {
            return if (id==1)
                Movie(
                    id,
                    "Movie $id",
                    "the movie $id overview",
                    "21/10/2023",
                    "poster url",
                    "backdrop url",
                    "Original Movie $id",
                    "en",
                    10.0,
                    8.0
                )
            else
                null
        }
    }
    private lateinit var detailViewModel: DetailViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        detailViewModel = DetailViewModel(getMovieDetailsUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get movie should emit loading and data states`() = runTest {
        // Arrange
        val events: ArrayList<DetailViewModel.MovieDetailUiState> = arrayListOf()
        val job = launch {
            detailViewModel.movieDetailStateFlow.collect { state ->
                events.add(state)
            }
        }

        // Act
        detailViewModel.getMovieDetails(1)
        advanceUntilIdle()

        // Assert
        assertEquals(
            DetailViewModel.MovieDetailUiState(
                false,
                Movie(
                    1,
                    "Movie 1",
                    "the movie 1 overview",
                    "21/10/2023",
                    "poster url",
                    "backdrop url",
                    "Original Movie 1",
                    "en",
                    10.0,
                    8.0
                )
            ),
            events[0]
        )
        job.cancel()
    }

    @Test
    fun `get movie emits loading and null`() = runTest {
        // Arrange
        val events: ArrayList<DetailViewModel.MovieDetailUiState> = arrayListOf()
        val job = launch {
            detailViewModel.movieDetailStateFlow.collect { state ->
                events.add(state)
            }
        }
        // Act
        detailViewModel.getMovieDetails(3)
        advanceUntilIdle()

        // Assert
        assertEquals(DetailViewModel.MovieDetailUiState(false, null), events[0])
        job.cancel()
    }
}