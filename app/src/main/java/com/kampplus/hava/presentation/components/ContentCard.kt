package com.kampplus.hava.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.core.ui.theme.HavaTheme

/**
 * Tekrar kullanılabilir içerik kartı bileşeni.
 *
 * @param title Kartın başlığı
 * @param description Kartın açıklama metni
 * @param modifier Dışarıdan uygulanacak Modifier
 */
@Composable
fun ContentCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true, name = "Kısa Metin Preview")
@Composable
private fun ContentCardShortTextPreview() {
    HavaTheme {
        ContentCard(
            title = "Kısa Başlık",
            description = "Bu kısa bir açıklamadır.",
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, name = "Uzun Metin Preview")
@Composable
private fun ContentCardLongTextPreview() {
    HavaTheme {
        ContentCard(
            title = "Çok Uzun Bir İçerik Başlığı İle Hava Durumu Bilgilendirmesi",
            description = "Bu kart bileşeni, uzun metinlerin ve detaylı açıklamaların ekran tasarımı üzerinde nasıl durduğunu, satır taşmalarını ve boşlukların tutarlılığını test etmek amacıyla oluşturulmuş uzun bir açıklama metni içermektedir.",
            modifier = Modifier.padding(16.dp),
        )
    }
}
