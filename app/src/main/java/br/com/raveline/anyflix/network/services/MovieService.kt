package br.com.raveline.anyflix.network.services

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

interface MovieService {
    @GET(movieApiServiceRequestKey)
    suspend fun getAllMovies(): List<MovieResponse>
}