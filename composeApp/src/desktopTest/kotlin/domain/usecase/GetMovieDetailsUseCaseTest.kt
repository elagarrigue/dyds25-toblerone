package domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
        val result = getMovieDetailsUseCase.invoke(1)

        //assert
        assertEquals(expectedMovie, result)
    }

    @Test
    fun `caso de uso con id invalida retorna nulo`() = runTest {
        //act
        val result = getMovieDetailsUseCase(999)

        //assert
        assertNull(result)
    }

    class FakeMoviesRepository : MoviesRepository {
        private val movies = mutableMapOf<Int, Movie>()

        override suspend fun getPopularMovies(): List<Movie> {
            return movies.values.toList()
        }

        override suspend fun getMovieDetails(id: Int): Movie? {
            return movies[id]
        }

        fun addMovie(movie: Movie) {
            movies[movie.id] = movie
        }
    }
}
