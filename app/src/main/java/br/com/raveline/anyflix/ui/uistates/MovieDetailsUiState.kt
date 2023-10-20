package br.com.raveline.anyflix.ui.uistates

import br.com.raveline.anyflix.model.Movie

sealed class MovieDetailsUiState {

    object Loading : MovieDetailsUiState()

    data class Success(
        val movie: Movie,
        val suggestedMovies: List<Movie> = emptyList()
    ) : MovieDetailsUiState()
}
