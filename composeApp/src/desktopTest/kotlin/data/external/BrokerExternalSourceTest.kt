package data.external

import edu.dyds.movies.data.external.BrokerExternalSource
import edu.dyds.movies.data.external.MovieByTitleRemoteSource
import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BrokerTest {
    private lateinit var movieBrokerExternalSource: BrokerExternalSource
    private lateinit var fakeTMDBMovies: MovieByTitleRemoteSource
    private lateinit var fakeOMDB: MovieByTitleRemoteSource

    @BeforeEach
    fun `set Up`() {
        fakeTMDBMovies = FakeTMDBSource()
        fakeOMDB = FakeOMDBSource()
        movieBrokerExternalSource = BrokerExternalSource(fakeTMDBMovies, fakeOMDB)
    }

    @Test
    fun `getmovieByTitle combines data`() = runTest {
        //act
        val movies = movieBrokerExternalSource.getMovieByTitle("F1")
        //assert
        assertEquals(
            "TMDB: F1 in TMDB\n" + "\n" + " OMDB: F1 in OMDB",
            movies?.overview
        )
    }

    @Test
    fun `getmovieByTitle only gets TMDB`() = runTest {
        //act
        val movies = movieBrokerExternalSource.getMovieByTitle("Squid Game")
        //assert
        assertEquals(
            "TMDB: Squid Game idk",
            movies?.overview
        )
    }

    @Test
    fun `getmovieByTitle only gets OMDB`() = runTest {
        //act
        val movies = movieBrokerExternalSource.getMovieByTitle("Nine Queens")
        //assert
        assertEquals(
            "OMDB: Nine Queens in OMDB",
            movies?.overview
        )
    }

    @Test
    fun `getmovieByTitle gets both nulls`() = runTest {
        //act & assert
        assertThrows<IllegalArgumentException> {
            movieBrokerExternalSource.getMovieByTitle("Unknown Movie")
        }

    }
}

class FakeTMDBSource : MovieByTitleRemoteSource {

    override suspend fun getMovieByTitle(title: String): Movie? {
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
        else {
            if (title == "F1")
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
        return null
    }

}

class FakeOMDBSource : MovieByTitleRemoteSource {
    override suspend fun getMovieByTitle(title: String): Movie? {
        return when {
            title == "F1" -> Movie(
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
            title == "Nine Queens" -> Movie(
                id = 2,
                title = "Nine Queens",
                overview = "Nine Queens in OMDB",
                releaseDate = "",
                poster = "",
                backdrop = "",
                originalTitle = "",
                originalLanguage = "",
                popularity = 75.0,
                voteAverage = 7.5
            )
            else -> null
        }

    }
}