package br.com.raveline.anyflix.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.raveline.anyflix.data.database.dao.MovieDao
import br.com.raveline.anyflix.data.database.entities.toMovie
import br.com.raveline.anyflix.data.model.Movie
import br.com.raveline.anyflix.navigation.movieIdArgument
import br.com.raveline.anyflix.ui.uistates.MovieDetailsUiState
import br.com.raveline.anyflix.ui.uistates.MovieDetailsUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dao: MovieDao
) : ViewModel() {
    private var currentUiStateJob: Job? = null

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(
        MovieDetailsUiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadUiState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadUiState() {
        currentUiStateJob?.cancel()
        currentUiStateJob = viewModelScope.launch {
            dao.findMovieById(
                requireNotNull(
                    savedStateHandle[movieIdArgument]
                )
            ).onStart {
                _uiState.update { MovieDetailsUiState.Loading }
            }.map {
                it.toMovie()
            }.flatMapLatest { movie ->
                dao.suggestedMovies(movie.id)
                    .map { suggestedMovies ->
                        Success(
                            movie = movie,
                            suggestedMovies = suggestedMovies,
                        )
                    }
            }.collectLatest { uiState ->
                _uiState.emit(uiState)
            }
        }
    }

    suspend fun addToMyList(movie: Movie) {
        dao.addToMyList(movie.id)
    }

    suspend fun removeFromMyList(movie: Movie) {
        dao.removeFromMyList(movie.id)
    }

    fun loadMovie() {
        loadUiState()
    }

}