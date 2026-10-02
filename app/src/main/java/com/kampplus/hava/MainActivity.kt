package com.kampplus.hava

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kampplus.hava.core.ui.theme.HavaTheme
import com.kampplus.hava.presentation.main.WeatherViewModel
import com.kampplus.hava.presentation.navigation.HavaNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userSettings by viewModel.userSettings.collectAsStateWithLifecycle()
            HavaTheme(themeMode = userSettings.themeMode) {
                HavaNavHost(viewModel = viewModel)
            }
        }
    }
}
