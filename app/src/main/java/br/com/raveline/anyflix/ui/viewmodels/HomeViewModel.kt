package br.com.raveline.anyflix.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.raveline.anyflix.database.dao.MovieDao
import br.com.raveline.anyflix.model.Movie
import br.com.raveline.anyflix.network.services.MovieService
import br.com.raveline.anyflix.network.services.toMovie
import br.com.raveline.anyflix.ui.uistates.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dao: MovieDao,
    private val service: MovieService
) : ViewModel() {

    private var currentUiStateJob: Job? = null
    private val _uiState = MutableStateFlow<HomeUiState>(
        HomeUiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadUiState()
    }

    private fun loadUiState() {
        currentUiStateJob?.cancel()
        currentUiStateJob = viewModelScope.launch {
            flow {
                val response = service.getAllMovies()
                val movies = response.map { it.toMovie() }
                emit(movies)
            }.onStart {
                _uiState.update { HomeUiState.Loading }
            }.map { movies ->
                if (movies.isEmpty()) {
                    emptyMap()
                } else {
                    createSections(movies)
                }
            }.collectLatest { sections ->
                if (sections.isEmpty()) {
                    _uiState.update {
                        HomeUiState.Empty
                    }
                } else {
                    val movie = sections
                        .entries.random()
                        .value.random()
                    _uiState.update {
                        HomeUiState.Success(
                            sections = sections,
                            mainBannerMovie = movie
                        )
                    }
                }
            }
        }
    }

    fun loadSections() {
        loadUiState()
    }

    private fun createSections(movies: List<Movie>) = mapOf(
        "Trends" to movies.shuffled().take(7),
        "News" to movies.shuffled().take(7),
        "Keep Watching" to movies.shuffled().take(7)
    )

}