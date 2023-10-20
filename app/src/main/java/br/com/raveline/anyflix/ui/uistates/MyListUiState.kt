package br.com.raveline.anyflix.ui.uistates

import br.com.raveline.anyflix.model.Movie


sealed class MyListUiState {

    object Loading : MyListUiState()

    object Empty : MyListUiState()

    data class Success(
        val movies: List<Movie> = emptyList()
    ) : MyListUiState()

}
