package data

import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.MovieByTitleRemoteSource
import edu.dyds.movies.data.external.PopularMoviesRemoteSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MoviesRepositoryTest {

    private lateinit var repository: MoviesRepositoryImpl
    private lateinit var fakeLocalSource: MoviesLocalSource
    private lateinit var fakeBroker: MovieByTitleRemoteSource
    private lateinit var fakeRemoteSource: FakeMoviesRemoteSource

    @BeforeEach
    fun `set up`() {
        fakeLocalSource = FakeMoviesLocalSource()
        fakeBroker = FakeBroker()
        fakeRemoteSource = FakeMoviesRemoteSource()
        repository = MoviesRepositoryImpl(
            fakeLocalSource,
            fakeRemoteSource,
            fakeBroker
        )
    }

    @Test
    fun `getPopular with cache full`() = runTest {
        //arrange
        fakeLocalSource.addAll(
            listOf(
                Movie(
                    1,
                    "cached",
                    "cached movie",
                    "01/01/01",
                    ".",
                    ".",
                    "cached",
                    "en",
                    7.0,
                    9.0
                )
            )
        )

        //act
        val movies = repository.getPopularMovies()

        //assert
        assertEquals(
            1,
            movies.size
        )
        assertEquals(
            "cached",
            movies[0].title
        )
    }

    @Test
    fun `getPopular with local empty and remote full`() = runTest {
        //arrange
        fakeRemoteSource.addToList(
            listOf(
                Movie(
                    1,
                    "remote",
                    "remote movie",
                    "01/01/01",
                    ".",
                    ".",
                    "remote",
                    "en",
                    7.0,
                    8.0
                )
            )
        )

        //act
        val movies = repository.getPopularMovies()

        //assert
        assertEquals(
            1,
            movies.size
        )
        assertEquals(
            "remote",
            movies[0].title
        )
    }

    @Test
    fun `getPopular with error on operation`() = runTest {
        //act
        val resultEmptySources = repository.getPopularMovies()

        //assert
        assertEquals(
            emptyList<Movie>(),
            resultEmptySources
        )
    }

    @Test
    fun `getDetails functioning correctly`() = runTest {
        //act
        val resultMovie = repository.getMovieDetails("m1")

        //assert
        assertEquals(
            "m1",
            resultMovie?.title
        )
    }

    @Test
    fun `getDetails with error on operation`() = runTest {
        //act
        val movieNull = repository.getMovieDetails("nonexistent movie")

        //assert
        assertEquals(
            null,
            movieNull
        )
    }
}

class FakeMoviesLocalSource : MoviesLocalSource {
    private val fakeCacheMovies: MutableList<Movie> = mutableListOf()

    override fun addAll(movies: List<Movie>) {
        fakeCacheMovies.addAll(movies)
    }

    override fun isEmpty(): Boolean {
        return fakeCacheMovies.isEmpty()
    }

    override fun getCacheMovies(): List<Movie> {
        return fakeCacheMovies
    }

}

class FakeBroker : MovieByTitleRemoteSource {
    private val remoteMovies: MutableList<Movie> = mutableListOf()

    fun addToList(listFake: List<Movie>) {
        remoteMovies.addAll(listFake)
    }

    override suspend fun getMovieByTitle(title: String): Movie {
        if (title == "m1")
            return Movie(
                1,
                "m1",
                "x",
                "x",
                "x",
                "x",
                "x",
                "x",
                7.0,
                8.0
            )
        throw RuntimeException()
    }

}

class FakeMoviesRemoteSource : PopularMoviesRemoteSource {
    private val remoteMovies: MutableList<Movie> = mutableListOf()

    fun addToList(listFake: List<Movie>) {
        remoteMovies.addAll(listFake)
    }

    override suspend fun getPopularMovies(): List<Movie> {
        if (remoteMovies.isNotEmpty())
            return remoteMovies
        throw Exception()
    }

}