package com.kampplus.hava.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.R
import com.kampplus.hava.core.ui.theme.HavaTheme

fun getWeatherBackgroundDrawable(code: Int?, isDay: Boolean = true): Int {
    val isClearSky = (code == 0) || (code == 1) || (code == null)
    if (!isDay && isClearSky) {
        return R.drawable.bg_weather_night
    }
    if ((!isDay) && (code == 2)) {
        return R.drawable.bg_weather_partly_cloudy_night
    }
    return when (code) {
        0, 1 -> R.drawable.bg_weather_sunny                    // Açık ve Güneşli (Gündüz)
        2 -> R.drawable.bg_weather_partly_cloudy               // Parçalı Bulutlu (Gündüz)
        3 -> R.drawable.bg_weather_overcast                   // Çok Bulutlu / Kapalı
        45, 48 -> R.drawable.bg_weather_foggy                 // Sisli
        51, 53, 55, 56, 57, 61, 63, 65, 80, 81, 82 -> R.drawable.bg_weather_rainy // Yağmurlu
        66, 67, 89, 90 -> R.drawable.bg_weather_hail          // Dolu Yağışlı
        71, 73, 75, 77, 85, 86 -> R.drawable.bg_weather_snowy // Karlı
        95, 96, 99 -> R.drawable.bg_weather_stormy            // Rüzgarlı / Fırtınalı
        else -> if (isDay) R.drawable.bg_weather_sunny else R.drawable.bg_weather_night
    }
}

/**
 * Tekrar kullanılabilir, hava durumuna ve gündüz/geceye özel canlı arka planlı içerik kartı bileşeni.
 *
 * @param title Kartın başlığı
 * @param description Kartın açıklama metni
 * @param weatherCode Hava durumu kodu (Open-Meteo)
 * @param isDay Gündüz mü gece mi bilgisi
 * @param isFavorite Favori olarak işaretli olup olmadığı
 * @param onFavoriteToggle Favori butonuna basıldığında çalışacak callback
 * @param modifier Dışarıdan uygulanacak Modifier
 */
@Composable
fun ContentCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    weatherCode: Int? = null,
    isDay: Boolean = true,
    isFavorite: Boolean = false,
    onFavoriteToggle: (() -> Unit)? = null,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val yellowStarColor = Color(0xFFFFC107)
    val backgroundRes = getWeatherBackgroundDrawable(weatherCode, isDay)

    val isStormy = (weatherCode == 95) || (weatherCode == 96) || (weatherCode == 99)
    val isNightClear = (!isDay) && ((weatherCode == 0) || (weatherCode == 1) || (weatherCode == null))
    val isPartlyCloudyNight = (!isDay) && (weatherCode == 2)
    val isNightOrStormy = isNightClear || isPartlyCloudyNight || isStormy
    val useDarkText = (!isDarkTheme) && (!isNightOrStormy)

    val titleColor = if (useDarkText) Color(0xFF1C1B1F) else Color.White
    val descriptionColor = if (useDarkText) Color(0xFF49454F) else Color(0xFFE6E1E5)
    val starUnselectedColor = if (useDarkText) Color(0xFF79747E) else Color(0xFFCAC4D0)

    val scrimAlpha = when {
        isDarkTheme -> 0.45f
        isNightOrStormy -> 0.25f
        else -> 0.10f
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AnimatedWeatherBackground(
                backgroundRes = backgroundRes,
                weatherCode = weatherCode,
                isDay = isDay,
                modifier = Modifier.matchParentSize(),
            )

            // Scrim overlay for high contrast and legibility
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = scrimAlpha)),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = titleColor,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = descriptionColor,
                    )
                }

                if (onFavoriteToggle != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.25f),
                                shape = CircleShape,
                            )
                            .clickable(onClick = onFavoriteToggle),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = if (isFavorite) "Favorilerden Çıkar" else "Favorilere Ekle",
                            tint = if (isFavorite) yellowStarColor else starUnselectedColor,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Gündüz Güneşli")
@Composable
private fun ContentCardSunnyPreview() {
    HavaTheme {
        ContentCard(
            title = "İzmir",
            description = "Sıcaklık: 28°C, Açık ve Güneşli",
            weatherCode = 0,
            isDay = true,
            isFavorite = false,
            onFavoriteToggle = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, name = "Gece Parçalı Bulutlu")
@Composable
private fun ContentCardPartlyCloudyNightPreview() {
    HavaTheme {
        ContentCard(
            title = "İstanbul",
            description = "Sıcaklık: 17°C, Parçalı Bulutlu",
            weatherCode = 2,
            isDay = false,
            isFavorite = true,
            onFavoriteToggle = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
