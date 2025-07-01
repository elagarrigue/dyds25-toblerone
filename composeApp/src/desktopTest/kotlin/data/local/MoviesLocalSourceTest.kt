package data.local

import edu.dyds.movies.data.local.MoviesLocalSource
import edu.dyds.movies.data.local.MoviesLocalSourceImpl
import edu.dyds.movies.domain.entity.Movie
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MoviesLocalSourceTest {
    private lateinit var localMovies: MoviesLocalSource

    @BeforeEach
    fun `set Up`() {
        localMovies = MoviesLocalSourceImpl()
    }

    @Test
    fun `adding Empty List`() {
        //arrange
        val empty = emptyList<Movie>()

        //act
        localMovies.addAll(empty)

        //assert
        assertEquals(
            emptyList<Movie>(),
            localMovies.getCacheMovies()
        )
    }

    @Test
    fun `adding Full List`() {
        //arrange
        val movie1 = Movie(
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
        val movie2 = Movie(
            1,
            "m2",
            "x",
            "x",
            "x",
            "x",
            "x",
            "x",
            7.0,
            8.0
        )
        val full = listOf<Movie>(movie1, movie2)

        //act
        localMovies.addAll(full)

        //assert
        assertEquals(
            full,
            localMovies.getCacheMovies()
        )
    }

    @Test
    fun `is Empty True`() {
        //arrange
        val empty = emptyList<Movie>()

        //act
        localMovies.addAll(empty)

        //assert
        assertEquals(
            true,
            localMovies.isEmpty()
        )
    }

    @Test
    fun `is Empty False`() {
        //arrange
        val movie1 = Movie(
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
        val movie2 = Movie(
            1,
            "m2",
            "x",
            "x",
            "x",
            "x",
            "x",
            "x",
            7.0,
            8.0
        )
        val full = listOf<Movie>(movie1, movie2)

        //act
        localMovies.addAll(full)

        //assert
        assertEquals(
            false,
            localMovies.isEmpty()
        )
    }
}