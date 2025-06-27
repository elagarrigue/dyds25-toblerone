package presentation.home

import edu.dyds.movies.domain.entity.*
import edu.dyds.movies.domain.usecase.PopularMoviesUseCase
import edu.dyds.movies.presentation.home.HomeViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var fakePopularMoviesUseCase: FakePopularMoviesUseCase

    @BeforeEach
    fun setUp() {
        fakePopularMoviesUseCase = FakePopularMoviesUseCase()
        homeViewModel = HomeViewModel(fakePopularMoviesUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `estado inicial debe tener valores default`() = runTest {
        //act
        val initialState = homeViewModel.moviesStateFlow.first()

        //assert
        assertTrue(initialState.isLoading)
        assertTrue(initialState.movies.isEmpty())
    }

    @Test
    fun `getPopularMovies emite estado final con peliculas`() = runTest {
        //arrange
        val expectedMovies = listOf(
            createQualifiedMovie(
                1,
                "Movie 1",
                7.5,
                true
            ),
            createQualifiedMovie(
                2,
                "Movie 2",
                6.0,
                true
            )
        )
        fakePopularMoviesUseCase.setMovies(expectedMovies)
        val states = mutableListOf<HomeViewModel.MoviesUiState>()

        //act
        val job = launch {
            homeViewModel.moviesStateFlow.collect { state ->
                states.add(state)
            }
        }
        homeViewModel.getPopularMovies()
        advanceUntilIdle()

        //assert
        assertEquals(1, states.size, "Expected exactly 1 state but got ${states.size}")

        val finalState = states[0]
        assertFalse(
            finalState.isLoading,
            "Final state should not be loading"
        )
        assertEquals(
            2,
            finalState.movies.size,
            "Final state should have 2 movies"
        )
        assertEquals(
            "Movie 1",
            finalState.movies[0].movie.title,
            "First movie title should match"
        )
        assertEquals(
            "Movie 2",
            finalState.movies[1].movie.title,
            "Second movie title should match"
        )

        job.cancel()
    }

    @Test
    fun `getPopularMovies emite estado de carga correctamente`() = runTest {
        //arrange
        val states = mutableListOf<HomeViewModel.MoviesUiState>()

        //act
        val job = launch {
            homeViewModel.moviesStateFlow.collect { state ->
                states.add(state)
            }
        }
        homeViewModel.getPopularMovies()
        advanceUntilIdle()

        //assert
        assertTrue(states.size == 1)
        assertFalse(
            states[0].isLoading,
            "First state should not be loading"
        )

        job.cancel()
    }

    @Test
    fun `getPopularMovies maneja lista vacia correctamente`() = runTest {
        //arrange
        fakePopularMoviesUseCase.setMovies(emptyList())
        val states = mutableListOf<HomeViewModel.MoviesUiState>()

        //act
        val job = launch {
            homeViewModel.moviesStateFlow.collect { state ->
                states.add(state)
            }
        }
        homeViewModel.getPopularMovies()
        advanceUntilIdle()

        //assert
        assertTrue(states.size == 1)
        val finalState = states.last()
        assertFalse(finalState.isLoading)
        assertTrue(finalState.movies.isEmpty())

        job.cancel()
    }

    private fun createQualifiedMovie(
        id: Int,
        title: String,
        voteAverage: Double,
        isGoodMovie: Boolean,
        overview: String = "Test overview",
        releaseDate: String = "2023-01-01",
        poster: String = "poster.jpg",
        backdrop: String? = "backdrop.jpg",
        originalTitle: String = title,
        originalLanguage: String = "en",
        popularity: Double = 100.0
    ) = QualifiedMovie(
        movie = Movie(
            id = id,
            title = title,
            overview = overview,
            releaseDate = releaseDate,
            poster = poster,
            backdrop = backdrop,
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            popularity = popularity,
            voteAverage = voteAverage
        ),
        isGoodMovie = isGoodMovie
    )

    class FakePopularMoviesUseCase : PopularMoviesUseCase {
        private var movies: List<QualifiedMovie> = emptyList()

        fun setMovies(movies: List<QualifiedMovie>) {
            this.movies = movies
        }

        override suspend operator fun invoke(): List<QualifiedMovie> {
            return movies
        }
    }
}