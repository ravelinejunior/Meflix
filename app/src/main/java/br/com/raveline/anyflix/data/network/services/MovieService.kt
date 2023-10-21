package br.com.raveline.anyflix.data.network.services

import br.com.raveline.anyflix.data.database.entities.MovieEntity
import br.com.raveline.anyflix.data.model.Movie
import br.com.raveline.anyflix.utils.movieApiServiceRequestKey
import br.com.raveline.anyflix.utils.movieIdApiServiceRequestKey
import br.com.raveline.anyflix.utils.myListApiServiceRequestKey
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    @GET(movieApiServiceRequestKey)
    suspend fun getAllMovies(): List<MovieResponse>

    @GET(myListApiServiceRequestKey)
    suspend fun getMyMovieList(): List<MovieResponse>

    @GET(movieIdApiServiceRequestKey)
    suspend fun getMoviesById(@Path("id") id: String): MovieResponse
}

data class MovieResponse(
    val id: String,
    val title: String,
    val image: String,
    val year: Int,
    val plot: String,
    val inMyList: Boolean
)

fun MovieResponse.toMovieEntity(): MovieEntity =
    MovieEntity(
        id = id,
        title = title,
        year = year,
        plot = plot,
        image = image,
        inMyList = inMyList,
    )

fun MovieResponse.toMovie(): Movie =
    Movie(
        id = id,
        title = title,
        year = year,
        plot = plot,
        image = image,
        inMyList = inMyList
    )

