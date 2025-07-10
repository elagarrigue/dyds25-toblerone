package data.external

import edu.dyds.movies.data.external.BrokerExternalSource
import edu.dyds.movies.data.external.MovieByTitleRemoteSource
import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BrokerExternalSourceTest {
    private lateinit var movieBrokerExternalSource: BrokerExternalSource
    private lateinit var fakeTMDBRemoteSource: MovieByTitleRemoteSource
    private lateinit var fakeOMDBRemoteSource: MovieByTitleRemoteSource

    @BeforeEach
    fun `set Up`() {
        fakeTMDBRemoteSource = FakeTMDBSource()
        fakeOMDBRemoteSource = FakeOMDBSource()
        movieBrokerExternalSource = BrokerExternalSource(fakeTMDBRemoteSource, fakeOMDBRemoteSource)
    }

    @Test
    fun `getmovieByTitle combines data`() = runTest {
        //act
        val movie = movieBrokerExternalSource.getMovieByTitle("F1")
        //assert
        assertEquals(
            Movie(
                id = 1,
                title = "F1",
                overview = "TMDB: F1 in TMDB\n" + "\n" + " OMDB: F1 in OMDB",
                releaseDate = "",
                poster = "",
                backdrop = "",
                originalTitle = "",
                originalLanguage = "",
                popularity = 87.5,
                voteAverage = 8.6
            ),
            movie
        )
    }

    @Test
    fun `getmovieByTitle only gets TMDB`() = runTest {
        //act
        val movie = movieBrokerExternalSource.getMovieByTitle("Squid Game")
        //assert
        assertEquals(
            Movie(
                id = 0,
                title = "Squid Game",
                overview = "TMDB: Squid Game idk",
                releaseDate = "",
                poster = "",
                backdrop = "",
                originalTitle = "",
                originalLanguage = "",
                popularity = 0.0,
                voteAverage = 0.0
            ),
            movie
        )
    }

    @Test
    fun `getmovieByTitle only gets OMDB`() = runTest {
        //act
        val movie = movieBrokerExternalSource.getMovieByTitle("Nine Queens")
        //assert
        assertEquals(
            Movie(
                id = 2,
                title = "Nine Queens",
                overview = "OMDB: Nine Queens in OMDB",
                releaseDate = "",
                poster = "",
                backdrop = "",
                originalTitle = "",
                originalLanguage = "",
                popularity = 75.0,
                voteAverage = 7.5
            ),
            movie
        )
    }

    @Test
    fun `getmovieByTitle gets both nulls`() = runTest {
        //act & assert
        assertThrows<IllegalArgumentException> {
            movieBrokerExternalSource.getMovieByTitle("Unknown Movie")
        }

    }

    @Test
    fun `getMovieByTitle returns OMDB when TMDB throws exception`() = runTest {
        movieBrokerExternalSource = BrokerExternalSource(ThrowingTMDBSource(), FakeOMDBSource())

        val movie = movieBrokerExternalSource.getMovieByTitle("Nine Queens")

        assertEquals(
            Movie(
                id = 2,
                title = "Nine Queens",
                overview = "OMDB: Nine Queens in OMDB",
                releaseDate = "",
                poster = "",
                backdrop = "",
                originalTitle = "",
                originalLanguage = "",
                popularity = 75.0,
                voteAverage = 7.5
            ),
            movie
        )
    }

    @Test
    fun `getMovieByTitle returns TMDB when OMDB throws exception`() = runTest {
        movieBrokerExternalSource = BrokerExternalSource(FakeTMDBSource(), ThrowingOMDBSource())

        val movie = movieBrokerExternalSource.getMovieByTitle("Squid Game")

        assertEquals(
            Movie(
                id = 0,
                title = "Squid Game",
                overview = "TMDB: Squid Game idk",
                releaseDate = "",
                poster = "",
                backdrop = "",
                originalTitle = "",
                originalLanguage = "",
                popularity = 0.0,
                voteAverage = 0.0
            ),
            movie
        )
    }
    @Test
    fun `getMovieByTitle throws when both sources fail`() = runTest {
        movieBrokerExternalSource = BrokerExternalSource(ThrowingTMDBSource(), ThrowingOMDBSource())

        assertThrows<IllegalArgumentException> {
            movieBrokerExternalSource.getMovieByTitle("Whatever")
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
        return when (title) {
            "F1" -> Movie(
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
            "Nine Queens" -> Movie(
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

class ThrowingTMDBSource : MovieByTitleRemoteSource {
    override suspend fun getMovieByTitle(title: String): Movie? {
        throw RuntimeException("TMDB error")
    }
}

class ThrowingOMDBSource : MovieByTitleRemoteSource {
    override suspend fun getMovieByTitle(title: String): Movie? {
        throw RuntimeException("OMDB error")
    }
}