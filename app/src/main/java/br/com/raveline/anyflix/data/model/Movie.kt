package br.com.raveline.anyflix.data.model

import br.com.raveline.anyflix.data.database.entities.MovieEntity
import java.util.UUID

data class Movie(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val year: Int,
    val plot: String,
    val image: String? = null,
    val inMyList: Boolean = false
)

fun Movie.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        year = year,
        plot = plot,
        image = image,
        inMyList = inMyList
    )
}