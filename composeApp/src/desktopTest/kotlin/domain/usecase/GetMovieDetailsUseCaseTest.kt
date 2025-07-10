package domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.collections.set

class GetMovieDetailsUseCaseTest {

    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var fakeMoviesRepository: FakeMoviesRepository

    @BeforeEach
    fun setUp() {
        fakeMoviesRepository = FakeMoviesRepository()
        getMovieDetailsUseCase = GetMovieDetailsUseCase(fakeMoviesRepository)
    }

    @Test
    fun `caso de uso con id valida devuelve detalles de pelicula`() = runTest {
        //arrange
        val expectedMovie = Movie(
            1,
            "Test Movie",
            "Test overview",
            "2024-01-01",
            "/test.jpg",
            "/backdrop.jpg",
            "Test Movie Original",
            "en",
            100.0,
            7.5
        )
        fakeMoviesRepository.addMovie(expectedMovie)

        //act
        val result = getMovieDetailsUseCase.invoke("Test Movie")

        //assert
        assertEquals(expectedMovie, result)
    }

    @Test
    fun `caso de uso con id invalida retorna nulo`() = runTest {
        //act
        val result = getMovieDetailsUseCase("inexistent movie")

        //assert
        assertNull(result)
    }

    class FakeMoviesRepository : MoviesRepository {
        private var movies: MutableList<Movie> = mutableListOf()

        fun setMovies(movies: MutableList<Movie>) {
            this.movies = movies
        }

        override suspend fun getPopularMovies(): List<Movie> {
            return movies
        }

        override suspend fun getMovieDetails(title: String): Movie? {
            return movies.find { it.title == title }
        }

        fun addMovie(movie: Movie) {
            movies.add(movie)
        }
    }
}
