package br.com.raveline.anyflix.ui.uistates

import br.com.raveline.anyflix.data.model.Movie

sealed class HomeUiState {

    object Loading : HomeUiState()

    object Empty : HomeUiState()

    data class Success(
        val sections: Map<String, List<Movie>> = emptyMap(),
        val mainBannerMovie: Movie? = null
    ) : HomeUiState()

}