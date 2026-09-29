package com.kampplus.hava.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.core.ui.state.LoadState
import com.kampplus.hava.core.ui.theme.HavaTheme
import com.kampplus.hava.domain.model.CityItem
import com.kampplus.hava.domain.model.ScreenData
import com.kampplus.hava.presentation.components.ContentCard

/**
 * Ana ekran Composable bileşeni.
 *
 * @param loadState Yükleme durumu
 * @param onCityClick Şehir seçildiğinde çalışacak callback
 * @param onRetry Hata veya boş durumda tekrar denemek için callback
 * @param modifier Dışarıdan uygulanacak Modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    loadState: LoadState,
    onCityClick: (String) -> Unit,
    onRetry: () -> Unit,
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            when (loadState) {
                is LoadState.Loading -> {
                    CircularProgressIndicator()
                }

                is LoadState.Content -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        loadState.data.cities.forEach { city ->
                            ContentCard(
                                title = city.name,
                                description = city.description,
                                modifier = Modifier.clickable { onCityClick(city.id) },
                            )
                        }
                    }
                }

                is LoadState.Empty -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Sonuç bulunamadı.",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRetry) {
                            Text(text = "Tekrar Dene")
                        }
                    }
                }

                is LoadState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = loadState.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRetry) {
                            Text(text = "Tekrar Dene")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
private fun HomeScreenLoadingPreview() {
    HavaTheme {
        HomeScreen(
            loadState = LoadState.Loading,
            onCityClick = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true, name = "Content State")
@Composable
private fun HomeScreenContentPreview() {
    HavaTheme {
        HomeScreen(
            loadState = LoadState.Content(
                ScreenData(
                    cities = listOf(
                        CityItem("istanbul", "İstanbul", "Sıcaklık: 22°C, Parçalı Bulutlu", 41.0, 28.9),
                        CityItem("ankara", "Ankara", "Sıcaklık: 18°C, Güneşli", 39.9, 32.8),
                    ),
                ),
            ),
            onCityClick = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
private fun HomeScreenEmptyPreview() {
    HavaTheme {
        HomeScreen(
            loadState = LoadState.Empty,
            onCityClick = {},
            onRetry = {},
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
private fun HomeScreenErrorPreview() {
    HavaTheme {
        HomeScreen(
            loadState = LoadState.Error("İnternet bağlantısı kurulamadı."),
            onCityClick = {},
            onRetry = {},
        )
    }
}
