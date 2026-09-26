package com.kampplus.hava.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.core.ui.theme.HavaTheme
import com.kampplus.hava.presentation.components.ContentCard

/**
 * Uygulamanın ana ekran Composable fonksiyonu.
 *
 * @param modifier Dışarıdan uygulanacak Modifier
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ContentCard(
                title = "İstanbul Hava Durumu",
                description = "Bugün hava parçalı bulutlu, sıcaklık 22°C civarında.",
            )
            ContentCard(
                title = "Günlük Özet ve Tavsiye",
                description = "Akşam saatlerinde rüzgarın şiddetini artırması bekleniyor. Dışarı çıkarken yanınıza hafif bir ceket almanız tavsiye edilir.",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    HavaTheme {
        MainScreen()
    }
}
