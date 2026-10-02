package com.kampplus.hava.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.R
import com.kampplus.hava.core.ui.theme.HavaTheme
import com.kampplus.hava.domain.model.CityItem

/**
 * Favori şehirler için sarı çerçeveli dinamik ölçeklenen bileşen.
 *
 * @param favoriteCities Favorilere eklenmiş şehirlerin eklenme sırasına göre listesi
 * @param onCityClick Şehir kartına tıklandığında çalışacak callback
 * @param onFavoriteToggle Favori butonuna tıklandığında çalışacak callback
 * @param modifier Dışarıdan uygulanacak Modifier
 */
@Composable
fun FavoriteCitiesSection(
    favoriteCities: List<CityItem>,
    onCityClick: (String) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val yellowStarColor = Color(0xFFFFC107)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, yellowStarColor),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.favorite_cities_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (favoriteCities.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.favorite_cities_empty_prompt),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    favoriteCities.forEach { city ->
                        ContentCard(
                            title = city.name,
                            description = city.description,
                            weatherCode = city.weatherCode,
                            isDay = city.isDay,
                            isFavorite = true,
                            onFavoriteToggle = { onFavoriteToggle(city.id) },
                            modifier = Modifier.clickable { onCityClick(city.id) },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Boş Favori Çerçevesi")
@Composable
private fun FavoriteCitiesSectionEmptyPreview() {
    HavaTheme {
        FavoriteCitiesSection(
            favoriteCities = emptyList(),
            onCityClick = {},
            onFavoriteToggle = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, name = "Dolu Favori Çerçevesi")
@Composable
private fun FavoriteCitiesSectionContentPreview() {
    HavaTheme {
        FavoriteCitiesSection(
            favoriteCities = listOf(
                CityItem("istanbul", "İstanbul", "Sıcaklık: 22°C, Parçalı Bulutlu", 41.0, 28.9, weatherCode = 1, isDay = true, isFavorite = true),
                CityItem("ankara", "Ankara", "Sıcaklık: 18°C, Güneşli", 39.9, 32.8, weatherCode = 0, isDay = true, isFavorite = true),
            ),
            onCityClick = {},
            onFavoriteToggle = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
