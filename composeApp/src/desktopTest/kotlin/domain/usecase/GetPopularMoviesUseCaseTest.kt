package domain.usecase

import edu.dyds.movies.domain.entity.Movie
import domain.usecase.GetMovieDetailsUseCaseTest.FakeMoviesRepository
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetPopularMoviesUseCaseTest {
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase
    private lateinit var fakeMoviesRepository: FakeMoviesRepository

    @BeforeEach
    fun setUp() {
        fakeMoviesRepository = FakeMoviesRepository()
        getPopularMoviesUseCase = GetPopularMoviesUseCase(fakeMoviesRepository)
    }

    @Test
    fun `caso de uso debe retornar lista de peliculas ordenada en descendente segun puntaje`() = runTest {
        // arrange
        val movies = mutableListOf(
            createMovie(id = 1, title = "Movie 1", voteAverage = 7.5),
            createMovie(id = 2, title = "Movie 2", voteAverage = 8.2),
            createMovie(id = 3, title = "Movie 3", voteAverage = 6.8)
        )
        fakeMoviesRepository.setMovies(movies)

        // act
        val result = getPopularMoviesUseCase()

        // assert
        assertEquals(3, result.size)
        assertEquals(8.2, result[0].movie.voteAverage)
        assertEquals("Movie 2", result[0].movie.title)
        assertEquals(7.5, result[1].movie.voteAverage)
        assertEquals("Movie 1", result[1].movie.title)
        assertEquals(6.8, result[2].movie.voteAverage)
        assertEquals("Movie 3", result[2].movie.title)
    }

    @Test
    fun `caso de uso califica correctamente pelicula segun puntaje`() = runTest {
        //arrange
        val movies = mutableListOf(
            createMovie(id = 1, title = "Good Movie", voteAverage = 7.0),
            createMovie(id = 2, title = "Exactly 6.0", voteAverage = 6.0),
            createMovie(id = 3, title = "Bad Movie", voteAverage = 5.9)
        )
        fakeMoviesRepository.setMovies(movies)

        //Act
        val result = getPopularMoviesUseCase()

        //assert
        assertEquals(3, result.size)
        assertTrue(result.find { it.movie.title == "Good Movie" }?.isGoodMovie == true)
        assertTrue(result.find { it.movie.title == "Exactly 6.0" }?.isGoodMovie == true)
        assertTrue(result.find { it.movie.title == "Bad Movie" }?.isGoodMovie == false)
    }

    @Test
    fun `caso de uso con lista de peliculas vacia`() = runTest{
        //arrange
        val emptyList: MutableList<Movie> = mutableListOf()
        fakeMoviesRepository.setMovies(emptyList)

        //act
        val result = getPopularMoviesUseCase()

        //assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `caso de uso con una sola pelicula`() = runTest {
        // arrange
        val singleMovieList = mutableListOf(createMovie(id = 1, title = "Single Movie", voteAverage = 7.0))
        fakeMoviesRepository.setMovies(singleMovieList)

        // act
        val result = getPopularMoviesUseCase()

        // assert
        assertEquals(1, result.size)
        assertEquals("Single Movie", result[0].movie.title)
        assertTrue(result[0].isGoodMovie)
    }

    @Test
    fun `caso de uso con peliculas con puntajes iguales`() = runTest {
        // arrange
        val movies = mutableListOf(
            createMovie(id = 1, title = "Movie A", voteAverage = 7.0),
            createMovie(id = 2, title = "Movie B", voteAverage = 7.0),
            createMovie(id = 3, title = "Movie C", voteAverage = 8.0)
        )
        fakeMoviesRepository.setMovies(movies)

        // act
        val result = getPopularMoviesUseCase()

        // assert
        assertEquals(3, result.size)
        assertEquals(8.0, result[0].movie.voteAverage)
        assertEquals(7.0, result[1].movie.voteAverage)
        assertEquals(7.0, result[2].movie.voteAverage)
        assertTrue(result.all { it.isGoodMovie })
    }

    private fun createMovie(
        id: Int,
        title: String,
        voteAverage: Double,
        overview: String = "Test overview",
        releaseDate: String = "2023-01-01",
        poster: String = "poster.jpg",
        backdrop: String? = "backdrop.jpg",
        originalTitle: String = title,
        originalLanguage: String = "en",
        popularity: Double = 100.0
    ) = Movie(
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
    )

}
