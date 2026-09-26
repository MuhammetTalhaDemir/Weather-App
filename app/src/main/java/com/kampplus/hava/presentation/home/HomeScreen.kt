package com.kampplus.hava.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.core.ui.theme.HavaTheme
import com.kampplus.hava.presentation.components.ContentCard
import com.kampplus.hava.presentation.main.MainUiState

/**
 * Ana ekran Composable bileşeni.
 *
 * @param uiState Ekran durumu
 * @param onCityClick Şehir seçildiğinde çalışacak callback
 * @param modifier Dışarıdan uygulanacak Modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: MainUiState,
    onCityClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Hava Durumu") },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            uiState.cityList.forEach { city ->
                ContentCard(
                    title = city.name,
                    description = city.description,
                    modifier = Modifier.clickable { onCityClick(city.id) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HavaTheme {
        HomeScreen(
            uiState = MainUiState(),
            onCityClick = {},
        )
    }
}
