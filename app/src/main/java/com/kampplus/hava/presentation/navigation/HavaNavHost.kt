package com.kampplus.hava.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kampplus.hava.presentation.detail.DetailScreen
import com.kampplus.hava.presentation.home.HomeScreen

/**
 * Uygulama navigasyon yöneticisi.
 *
 * @param modifier Dışarıdan uygulanacak Modifier
 * @param navController Navigasyon kontrolcüsü
 */
@Composable
fun HavaNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier,
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onCityClick = { cityId ->
                    navController.navigate(Screen.Detail(cityId = cityId))
                },
            )
        }
        composable<Screen.Detail> { backStackEntry ->
            val detailRoute = backStackEntry.toRoute<Screen.Detail>()
            DetailScreen(
                cityId = detailRoute.cityId,
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}
