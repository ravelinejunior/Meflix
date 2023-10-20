package br.com.raveline.anyflix.network.services

import br.com.raveline.anyflix.model.Movie
import br.com.raveline.anyflix.utils.movieApiServiceRequestKey
import retrofit2.http.GET

data class MovieResponse(
    val id: String,
    val title: String,
    val image: String,
    val year: Int,
    val plot: String,
    val inMyList: Boolean
)

fun MovieResponse.toMovie() : Movie =
    Movie(
        id = id,
        title = title,
        year = year,
        plot = plot,
        image = image,
        inMyList = inMyList
    )

interface MovieService {
    @GET(movieApiServiceRequestKey)
    suspend fun getAllMovies(): List<MovieResponse>
}