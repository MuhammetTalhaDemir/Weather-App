package com.kampplus.hava.presentation.navigation

import androidx.compose.runtime.Composable
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

    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier,
    ) {
        composable<Screen.Home> {
            HomeScreen(
                loadState = loadState,
                onCityClick = { cityId ->
                    navController.navigate(Screen.Detail(cityId = cityId))
                },
                onRetry = {
                    viewModel.loadData()
                },
            )
        }
        composable<Screen.Detail> { backStackEntry ->
            val detailRoute = backStackEntry.toRoute<Screen.Detail>()
            DetailScreen(
                cityId = detailRoute.cityId,
                loadState = loadState,
                onBackClick = {
                    navController.popBackStack()
                },
                onRetry = {
                    viewModel.loadData()
                },
            )
        }
    }
}
