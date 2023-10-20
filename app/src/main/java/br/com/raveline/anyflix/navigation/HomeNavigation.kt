package br.com.raveline.anyflix.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.raveline.anyflix.model.Movie
import br.com.raveline.anyflix.ui.screens.HomeScreen
import br.com.raveline.anyflix.ui.viewmodels.HomeViewModel

internal const val homeRoute = "home"

fun NavGraphBuilder.homeScreen(
    onNavigateToMovieDetails: (Movie) -> Unit,
) {
    composable(homeRoute) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        HomeScreen(
            uiState = uiState,
            onMovieClick = onNavigateToMovieDetails,
            onRetryLoadSections = {
                viewModel.loadSections()
            }
        )
    }
}

fun NavController.navigateToHome(
    navOptions: NavOptions? = null
) {
    navigate(homeRoute, navOptions)
}