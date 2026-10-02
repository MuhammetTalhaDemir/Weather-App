package com.kampplus.hava.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kampplus.hava.R
import com.kampplus.hava.domain.model.HourlyForecast

fun getWeatherIconDrawable(code: Int?): Int = when (code) {
    0 -> R.drawable.ic_weather_sunny                      // Güneşli
    1, 2 -> R.drawable.ic_weather_partly_cloudy           // Az / Parçalı Bulutlu
    3 -> R.drawable.ic_weather_cloudy                     // Çok Bulutlu
    45, 48 -> R.drawable.ic_weather_foggy                 // Sisli
    in 51..67, in 80..82 -> R.drawable.ic_weather_rainy   // Yağmurlu
    in 71..77, in 85..86 -> R.drawable.ic_weather_snowy   // Karlı
    95, 96, 99 -> R.drawable.ic_weather_stormy            // Fırtınalı
    else -> R.drawable.ic_weather_sunny
}

/**
 * Saatlik sıcaklık tahminleri kaydırmalı liste bileşeni.
 */
@Composable
fun HourlyForecastSection(
    hourlyForecasts: List<HourlyForecast>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.section_hourly),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 4.dp),
        ) {
            items(hourlyForecasts) { item ->
                HourlyForecastCard(item = item)
            }
        }
    }
}

@Composable
private fun HourlyForecastCard(
    item: HourlyForecast,
) {
    Card(
        modifier = Modifier.width(72.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = item.time,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Image(
                painter = painterResource(id = getWeatherIconDrawable(item.weatherCode)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.temperature,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
