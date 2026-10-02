package com.kampplus.hava.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.R
import com.kampplus.hava.core.ui.state.LoadState
import com.kampplus.hava.core.ui.theme.HavaTheme
import com.kampplus.hava.domain.model.CityItem
import com.kampplus.hava.domain.model.ScreenData
import com.kampplus.hava.presentation.components.ContentCard
import com.kampplus.hava.presentation.components.FavoriteCitiesSection
import com.kampplus.hava.presentation.components.SearchBarComponent

/**
 * Ana ekran Composable bileşeni.
 *
 * @param loadState Yükleme durumu
 * @param searchQuery Arama metni
 * @param onSearchQueryChange Arama metni değiştiğinde çalışacak callback
 * @param onCityClick Şehir seçildiğinde çalışacak callback
 * @param onFavoriteToggle Favori butonu basıldığında çalışacak callback
 * @param onSettingsClick Ayarlar butonuna basıldığında çalışacak callback
 * @param onRetry Hata veya boş durumda tekrar denemek için callback
 * @param modifier Dışarıdan uygulanacak Modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    loadState: LoadState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCityClick: (String) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_title)) },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.settings_title),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            SearchBarComponent(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                modifier = Modifier.padding(vertical = 8.dp),
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                when (loadState) {
                    is LoadState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is LoadState.Content -> {
                        val filteredCities = if (searchQuery.isBlank()) {
                            loadState.data.cities
                        } else {
                            loadState.data.cities.filter { city ->
                                city.name.contains(searchQuery, ignoreCase = true)
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            item(key = "favorite_cities_section") {
                                FavoriteCitiesSection(
                                    favoriteCities = loadState.data.favoriteCities,
                                    onCityClick = onCityClick,
                                    onFavoriteToggle = onFavoriteToggle,
                                )
                            }

                            if (filteredCities.isEmpty()) {
                                item(key = "no_search_results") {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 24.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.no_search_results),
                                            style = MaterialTheme.typography.bodyLarge,
                                        )
                                    }
                                }
                            } else {
                                items(
                                    items = filteredCities,
                                    key = { it.id },
                                ) { city ->
                                    ContentCard(
                                        title = city.name,
                                        description = city.description,
                                        weatherCode = city.weatherCode,
                                        isDay = city.isDay,
                                        isFavorite = city.isFavorite,
                                        onFavoriteToggle = { onFavoriteToggle(city.id) },
                                        modifier = Modifier.clickable { onCityClick(city.id) },
                                    )
                                }
                            }
                        }
                    }

                    is LoadState.Empty -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = stringResource(id = R.string.no_results_found),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onRetry) {
                                Text(text = stringResource(id = R.string.action_retry))
                            }
                        }
                    }

                    is LoadState.Error -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp),
                        ) {
                            Text(
                                text = loadState.message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onRetry) {
                                Text(text = stringResource(id = R.string.action_retry))
                            }
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
            searchQuery = "",
            onSearchQueryChange = {},
            onCityClick = {},
            onFavoriteToggle = {},
            onSettingsClick = {},
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
                        CityItem("istanbul", "İstanbul", "Sıcaklık: 22°C, Parçalı Bulutlu", 41.0, 28.9, weatherCode = 1, isDay = true, isFavorite = true),
                        CityItem("ankara", "Ankara", "Sıcaklık: 18°C, Güneşli", 39.9, 32.8, weatherCode = 0, isDay = true, isFavorite = false),
                    ),
                    favoriteCities = listOf(
                        CityItem("istanbul", "İstanbul", "Sıcaklık: 22°C, Parçalı Bulutlu", 41.0, 28.9, weatherCode = 1, isDay = true, isFavorite = true),
                    ),
                ),
            ),
            searchQuery = "",
            onSearchQueryChange = {},
            onCityClick = {},
            onFavoriteToggle = {},
            onSettingsClick = {},
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
            searchQuery = "",
            onSearchQueryChange = {},
            onCityClick = {},
            onFavoriteToggle = {},
            onSettingsClick = {},
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
            searchQuery = "",
            onSearchQueryChange = {},
            onCityClick = {},
            onFavoriteToggle = {},
            onSettingsClick = {},
            onRetry = {},
        )
    }
}
