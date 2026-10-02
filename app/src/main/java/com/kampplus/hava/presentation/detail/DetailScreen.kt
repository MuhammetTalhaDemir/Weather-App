package com.kampplus.hava.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.core.ui.state.LoadState
import com.kampplus.hava.core.ui.theme.HavaTheme
import com.kampplus.hava.domain.model.CityDetailData
import com.kampplus.hava.domain.model.CityItem
import com.kampplus.hava.domain.model.DailyForecast
import com.kampplus.hava.domain.model.HourlyForecast
import com.kampplus.hava.domain.model.ScreenData
import com.kampplus.hava.presentation.components.ContentCard
import com.kampplus.hava.presentation.components.DailyForecastSection
import com.kampplus.hava.presentation.components.HourlyForecastSection
import com.kampplus.hava.presentation.components.WeatherMetricsCard

/**
 * Tasarıma ve ekran görüntüsüne özel Detay Ekranı Composable bileşeni.
 *
 * @param cityId Detayı gösterilecek şehrin kimliği
 * @param loadState Yükleme durumu
 * @param onBackClick Geri butonuna basıldığında çalışacak callback
 * @param onFavoriteToggle Favori butonuna basıldığında çalışacak callback
 * @param onRetry Hata durumunda tekrar denemek için callback
 * @param modifier Dışarıdan uygulanacak Modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    cityId: String,
    loadState: LoadState,
    onBackClick: () -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val yellowStarColor = Color(0xFFFFC107)
    val selectedCity = (loadState as? LoadState.Content)?.data?.cities?.find { it.id == cityId }
    val detailData = (loadState as? LoadState.Content)?.data?.cityDetails?.get(cityId)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = selectedCity?.name ?: cityId.replaceFirstChar { it.uppercase() }) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                        )
                    }
                },
                actions = {
                    if (selectedCity != null) {
                        IconButton(onClick = { onFavoriteToggle(selectedCity.id) }) {
                            Icon(
                                imageVector = if (selectedCity.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = if (selectedCity.isFavorite) "Favorilerden Çıkar" else "Favorilere Ekle",
                                tint = if (selectedCity.isFavorite) yellowStarColor else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when (loadState) {
                is LoadState.Loading -> {
                    CircularProgressIndicator()
                }

                is LoadState.Content -> {
                    if (selectedCity != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            // 1. Üst Şehir Kartı
                            ContentCard(
                                title = "${selectedCity.name}, Türkiye",
                                description = selectedCity.description,
                                weatherCode = selectedCity.weatherCode,
                                isDay = selectedCity.isDay,
                            )

                            // 2. Hissedilen, Nem, Rüzgâr
                            WeatherMetricsCard(
                                apparentTemp = detailData?.apparentTemp ?: "--°",
                                humidity = detailData?.humidity ?: "--",
                                windSpeed = detailData?.windSpeed ?: "--",
                            )

                            // 3. Saatlik Sıcaklıklar
                            if (detailData?.hourlyForecasts?.isNotEmpty() == true) {
                                HourlyForecastSection(
                                    hourlyForecasts = detailData.hourlyForecasts,
                                )
                            }

                            // 4. 7 Günlük Tahminler
                            if (detailData?.dailyForecasts?.isNotEmpty() == true) {
                                DailyForecastSection(
                                    dailyForecasts = detailData.dailyForecasts,
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Sonuç bulunamadı.")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onRetry) {
                                Text(text = "Tekrar Dene")
                            }
                        }
                    }
                }

                is LoadState.Empty -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Sonuç bulunamadı.")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRetry) {
                            Text(text = "Tekrar Dene")
                        }
                    }
                }

                is LoadState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = loadState.message,
                            color = MaterialTheme.colorScheme.error,
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

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    val sampleCity = CityItem("izmir", "İzmir", "Sıcaklık: 18°C, Açık ve Güneşli", 38.4, 27.1, weatherCode = 0, isDay = true, isFavorite = true)
    val sampleDetail = CityDetailData(
        city = sampleCity,
        apparentTemp = "18°",
        humidity = "%66",
        windSpeed = "5 km/sa",
        hourlyForecasts = listOf(
            HourlyForecast("23:00", "18°", 0),
            HourlyForecast("00:00", "18°", 0),
            HourlyForecast("01:00", "17°", 0),
            HourlyForecast("02:00", "17°", 0),
            HourlyForecast("03:00", "16°", 1),
        ),
        dailyForecasts = listOf(
            DailyForecast("Bugün", 13, 1, "16°", "25°"),
            DailyForecast("Perşembe", 5, 1, "16°", "23°"),
            DailyForecast("Cuma", null, 1, "13°", "24°"),
            DailyForecast("Cumartesi", 8, 1, "15°", "25°"),
            DailyForecast("Pazar", 3, 1, "15°", "26°"),
            DailyForecast("Pazartesi", null, 0, "14°", "24°"),
            DailyForecast("Salı", null, 1, "14°", "24°"),
        ),
    )

    HavaTheme {
        DetailScreen(
            cityId = "izmir",
            loadState = LoadState.Content(
                ScreenData(
                    cities = listOf(sampleCity),
                    favoriteCities = listOf(sampleCity),
                    cityDetails = mapOf("izmir" to sampleDetail),
                ),
            ),
            onBackClick = {},
            onFavoriteToggle = {},
            onRetry = {},
        )
    }
}
