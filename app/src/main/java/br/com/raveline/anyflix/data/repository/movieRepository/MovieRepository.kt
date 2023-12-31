package br.com.raveline.anyflix.data.repository.movieRepository

import android.util.Log
import br.com.raveline.anyflix.data.database.dao.MovieDao
import br.com.raveline.anyflix.data.database.entities.toMovie
import br.com.raveline.anyflix.data.model.Movie
import br.com.raveline.anyflix.data.network.services.MovieService
import br.com.raveline.anyflix.data.network.services.toMovieEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class MovieRepository @Inject constructor(
    private val dao: MovieDao,
    private val service: MovieService
) {
    private val TAG: String = MovieRepository::class.java.simpleName

    suspend fun findMovieSections(): Flow<Map<String, List<Movie>>> {

        CoroutineScope(coroutineContext).launch(IO) {
            try {
                val response = service.getAllMovies()
                val entities = response.map { it.toMovieEntity() }
                if (entities.isNotEmpty()) {
                    dao.saveAllMovies(*entities.toTypedArray())
                } else {
                    Log.i(TAG + "_findMovieSections", "No movie found to save.")
                }
            } catch (e: ConnectException) {
                Log.e(TAG + "_findMovieSections", "ConnectException throw: ${e.message}")
            } catch (e: SocketTimeoutException) {
                Log.e(TAG + "_findMovieSections", "SocketTimeoutException throw: ${e.message}")
            } catch (e: Exception) {
                Log.e(TAG + "_findMovieSections", "Exception throw: ${e.message}")
            }
        }

        return dao.findAll().map { entities ->
            val movies = entities.map { it.toMovie() }
            if (movies.isEmpty()) {
                Log.i(TAG + "_findMovieSections", "No movie found.")
                emptyMap()
            } else {
                createSections(movies)
            }
        }
    }

    private fun createSections(movies: List<Movie>) = mapOf(
        "Trends" to movies.shuffled().take(7),
        "News" to movies.shuffled().take(7),
        "Keep Watching" to movies.shuffled().take(7)
    )

    suspend fun getMyList(): Flow<List<Movie>> {
        CoroutineScope(coroutineContext).launch(IO) {
            try {
                val response = service.getMyMovieList()
                val entities = response.map { it.toMovieEntity() }
                if (entities.isNotEmpty()) {
                    dao.saveAllMovies(*entities.toTypedArray())
                } else {
                    Log.i(TAG + "_myList", "No movie found to save.")
                }
            } catch (e: ConnectException) {
                Log.e(TAG + "_myList", "ConnectException throw: ${e.message}")
            } catch (e: SocketTimeoutException) {
                Log.e(TAG + "_myList", "SocketTimeoutException throw: ${e.message}")
            } catch (e: Exception) {
                Log.e(TAG + "_myList", "Exception throw: ${e.message}")
            }

        }

        return dao.myList().map { entities ->
            entities.map { it.toMovie() }
        }
    }

    suspend fun getMovieById(id: String): Flow<Movie> {
        CoroutineScope(coroutineContext).launch(IO) {
            try {
                val response = service.getMoviesById(id)
                val entity = response.toMovieEntity()
                dao.save(entity)
            } catch (e: ConnectException) {
                Log.e(TAG + "_getMovieById", "ConnectException throw: ${e.message}")
            } catch (e: SocketTimeoutException) {
                Log.e(TAG + "_getMovieById", "SocketTimeoutException throw: ${e.message}")
            } catch (e: Exception) {
                Log.e(TAG + "_getMovieById", "Exception throw: ${e.message}")
            }
        }

        return dao.findMovieById(id)
            .map {
                it.toMovie()
            }
    }

    fun getSuggestedMovie(id: String): Flow<List<Movie>> =
        dao.suggestedMovies(id)

    suspend fun addToMyList(id: String) {
        CoroutineScope(coroutineContext).launch(IO) {
            try {
                service.addMovieToMyList(id)
                dao.addToMyList(id)
            } catch (e: ConnectException) {
                Log.e(TAG + "_addToMyList", "ConnectException throw: ${e.message}")
            } catch (e: SocketTimeoutException) {
                Log.e(TAG + "_addToMyList", "SocketTimeoutException throw: ${e.message}")
            } catch (e: Exception) {
                Log.e(TAG + "_addToMyList", "Exception throw: ${e.message}")
            }
        }
    }

    suspend fun removeFromMyList(id: String) {
        CoroutineScope(coroutineContext).launch(IO) {
            try {
                service.removeMovieFromMyList(id)
                dao.removeFromMyList(id)
            } catch (e: ConnectException) {
                Log.e(TAG + "_removeFromMyList", "ConnectException throw: ${e.message}")
            } catch (e: SocketTimeoutException) {
                Log.e(TAG + "_removeFromMyList", "SocketTimeoutException throw: ${e.message}")
            } catch (e: Exception) {
                Log.e(TAG + "_removeFromMyList", "Exception throw: ${e.message}")
            }
        }
    }

}






