package data.external

import edu.dyds.movies.data.external.BrokerExternalSource
import edu.dyds.movies.data.external.MovieByTitleRemoteSource
import edu.dyds.movies.data.external.PopularMoviesRemoteSource
import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BrokerTest {
    private lateinit var movieBrokerExternalSource: BrokerExternalSource
    private lateinit var fakeTMDBMovies: MovieByTitleRemoteSource
    private lateinit var fakeTMDBPopular: PopularMoviesRemoteSource
    private lateinit var fakeOMDB: MovieByTitleRemoteSource

    @BeforeEach
    fun `set Up`() {
        fakeTMDBMovies = FakeTMDBSource()
        fakeTMDBPopular = FakeTMDBSource()
        fakeOMDB = FakeOMDBSource()
        movieBrokerExternalSource = BrokerExternalSource(fakeTMDBPopular, fakeTMDBMovies, fakeOMDB)
    }

    @Test
    fun `getPopularMovies return full list`() = runTest {
        //act
        val movies = movieBrokerExternalSource.getPopularMovies()

        //assert
        assertEquals(
            "F1",
            movies.first().title
        )
    }

    @Test
    fun `getmovieByTitle combines data`() = runTest {
        //act
        val movies = movieBrokerExternalSource.getMovieByTitle("F1")
        //assert
        assertEquals(
            "TMDB: F1 in TMDB\n" + "\n" + " OMDB: F1 in OMDB",
            movies.overview
        )
    }
    @Test
    fun `getmovieByTitle only gets TMDB`() = runTest {
        //act
        val movies = movieBrokerExternalSource.getMovieByTitle("Squid Game")
        //assert
        assertEquals(
            "TMDB: Squid Game idk\n" + "\n" + " OMDB: No plot available",
            movies.overview
        )
    }
}

class FakeTMDBSource : PopularMoviesRemoteSource, MovieByTitleRemoteSource {
    override suspend fun getPopularMovies(): List<Movie> {
        val movie = Movie(
            id = 1,
            title = "F1",
            overview = "F1 in TMDB",
            releaseDate = "",
            poster = "",
            backdrop = "",
            originalTitle = "",
            originalLanguage = "",
            popularity = 87.5,
            voteAverage = 8.6
        )
        return listOf(movie)
    }

    override suspend fun getMovieByTitle(title: String): Movie {
        if (title == "Squid Game")
            return Movie(
                id = 0,
                title = "Squid Game",
                overview = "Squid Game idk",
                releaseDate = "",
                poster = "",
                backdrop = "",
                originalTitle = "",
                originalLanguage = "",
                popularity = 0.0,
                voteAverage = 0.0
            )
        else
            return Movie(
                id = 1,
                title = "F1",
                overview = "F1 in TMDB",
                releaseDate = "",
                poster = "",
                backdrop = "",
                originalTitle = "",
                originalLanguage = "",
                popularity = 87.5,
                voteAverage = 8.6
            )
    }

}

class FakeOMDBSource : MovieByTitleRemoteSource {
    override suspend fun getMovieByTitle(title: String): Movie {
        if (title == "Squid Game")
            return Movie(
                id = 0,
                title = "Unknown Title",
                overview = "No plot available",
                releaseDate = "",
                poster = "",
                backdrop = "",
                originalTitle = "",
                originalLanguage = "",
                popularity = 0.0,
                voteAverage = 0.0
            )
        else
            return Movie(
                id = 1,
                title = "F1",
                overview = "F1 in OMDB",
                releaseDate = "",
                poster = "",
                backdrop = "",
                originalTitle = "",
                originalLanguage = "",
                popularity = 87.5,
                voteAverage = 8.6
            )
    }
}