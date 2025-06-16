import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.MoviesRemoteSource
import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.data.local.MoviesLocalSourceImpl
import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MoviesRepositoryTest {

    private lateinit var repository: MoviesRepositoryImpl
    private lateinit var fakeLocalSource: MoviesLocalSource
    private lateinit var fakeRemoteSource: FakeMoviesRemoteSource

    @BeforeEach
    fun `set Up`(){
        fakeLocalSource= MoviesLocalSourceImpl()
        fakeRemoteSource= FakeMoviesRemoteSource()
        repository= MoviesRepositoryImpl(fakeLocalSource,fakeRemoteSource)
    }
    @Test
    fun `getPopular with cache full`() = runTest{
        //arrange
        fakeLocalSource.addAll(listOf(Movie(1, "cached", "", "", "", "", "", "", 0.0, 0.0)))
        //act
        val movies = repository.getPopularMovies()
        //assert
        assertEquals(1, movies.size)
        assertEquals("cached", movies[0].title)
    }
    @Test
    fun `getPopular with cache empty`() = runTest{
        //arrange
        fakeRemoteSource.addToList(listOf(Movie(1, "remote", "", "", "", "", "", "", 0.0, 0.0)))
        //act
        val movies = repository.getPopularMovies()
        //assert
        assertEquals(1, movies.size)
        assertEquals("remote", movies[0].title)
    }
    @Test
    fun `getPopular  error`() = runTest{
        //act
        val resultEmptySources = repository.getPopularMovies()
        //assert
        assertEquals(emptyList<Movie>(), resultEmptySources)
        //TODO revisar por que no tira el error o por que no lo podemos leer
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
        val movieNull = repository.getMovieDetails(500)
        //assert
        assertEquals(null, movieNull)
        //TODO revisar por que no tira el error o por que no lo podemos leer
    }
}

class FakeMoviesRemoteSource: MoviesRemoteSource{
    private val remoteMovies: MutableList<Movie> = mutableListOf()

    fun addToList(listFake: List<Movie>){
        remoteMovies.addAll(listFake)
    }

    override suspend fun getTMDBPopularMovies(): List<Movie> {
        if (!remoteMovies.isEmpty())
            return remoteMovies
        throw Exception()
    }

    override suspend fun getTMDBMovieDetails(id: Int): Movie {
        if (id==1)
            return Movie(1, "m1", "x", "x", "x", "x","x","x",7.0,8.0)
        throw RuntimeException()
    }

}