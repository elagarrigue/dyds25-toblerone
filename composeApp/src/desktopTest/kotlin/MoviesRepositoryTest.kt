import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.MoviesRemoteSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.data.local.MoviesLocalSourceImpl
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MoviesRepositoryTest {

    private lateinit var repository: MoviesRepositoryImpl
    private lateinit var fakeLocalSource: FakeMoviesLocalSource
    private lateinit var fakeRemoteSource: FakeMoviesRemoteSource

    @BeforeEach
    fun `set Up`(){
        fakeLocalSource= FakeMoviesLocalSource()
        fakeRemoteSource= FakeMoviesRemoteSource()
        repository= MoviesRepositoryImpl(fakeLocalSource,fakeRemoteSource)
    }
    @Test
    fun `getPopular with cache full`(){

    }
    @Test
    fun `getPopular with cache empty`(){

    }
    @Test
    fun `getPopular  error`(){

    }
    @Test
    fun `getDetails normal`()=runTest{
        //act
        val resultMovie = repository.getMovieDetails(1)
        //assert
        assertEquals("m1", resultMovie?.title)
    }
    @Test
    fun `getDetails error`() = runTest {
        //act
        val exception = assertThrows<RuntimeException> { repository.getMovieDetails(500) }
        //assert
        assertEquals("Error fetching movie details for id 2: ", exception.message)
    }
}

class FakeMoviesLocalSource: MoviesLocalSource{
    override fun addAll(movies: List<Movie>) {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCacheMovies(): List<Movie> {
        TODO("Not yet implemented")
    }
}

class FakeMoviesRemoteSource: MoviesRemoteSource{
    override suspend fun getTMDBPopularMovies(): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getTMDBMovieDetails(id: Int): Movie {
        if (id==1)
            return Movie(1, "m1", "x", "x", "x", "x","x","x",7.0,8.0)
        throw RuntimeException()
    }

}