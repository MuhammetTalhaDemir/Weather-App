package com.kampplus.hava.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kampplus.hava.presentation.detail.DetailScreen
import com.kampplus.hava.presentation.home.HomeScreen
import com.kampplus.hava.presentation.main.WeatherViewModel
import com.kampplus.hava.presentation.settings.SettingsScreen

/**
 * Uygulama navigasyon yöneticisi ve durum bağlayıcısı.
 *
 * @param modifier Dışarıdan uygulanacak Modifier
 * @param navController Navigasyon kontrolcüsü
 * @param viewModel Ortak ViewModel
 */
@Composable
fun HavaNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val loadState by viewModel.loadState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val userSettings by viewModel.userSettings.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier,
    ) {
        composable<Screen.Home> {
            HomeScreen(
                loadState = loadState,
                searchQuery = searchQuery,
                onSearchQueryChange = { query ->
                    viewModel.onSearchQueryChange(query)
                },
                onCityClick = { cityId ->
                    viewModel.loadCityDetail(cityId)
                    navController.navigate(Screen.Detail(cityId = cityId))
                },
                onFavoriteToggle = { cityId ->
                    viewModel.toggleFavorite(cityId)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings)
                },
                onRetry = {
                    viewModel.loadData()
                },
            )
        }
        composable<Screen.Detail> { backStackEntry ->
            val detailRoute = backStackEntry.toRoute<Screen.Detail>()
            LaunchedEffect(detailRoute.cityId) {
                viewModel.loadCityDetail(detailRoute.cityId)
            }
            DetailScreen(
                cityId = detailRoute.cityId,
                loadState = loadState,
                onBackClick = {
                    navController.popBackStack()
                },
                onFavoriteToggle = { cityId ->
                    viewModel.toggleFavorite(cityId)
                },
                onRetry = {
                    viewModel.loadData()
                    viewModel.loadCityDetail(detailRoute.cityId)
                },
            )
        }
        composable<Screen.Settings> {
            SettingsScreen(
                userSettings = userSettings,
                onUpdateSettings = { settings ->
                    viewModel.updateUserSettings(settings)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}
