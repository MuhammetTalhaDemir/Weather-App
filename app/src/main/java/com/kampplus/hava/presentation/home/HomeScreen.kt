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

/**
 * Ana ekran Composable bileşeni.
 *
 * @param onCityClick Şehir seçildiğinde çalışacak callback (seçilen şehrin kimliğini iletir)
 * @param modifier Dışarıdan uygulanacak Modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
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
            ContentCard(
                title = "İstanbul",
                description = "Bugün hava parçalı bulutlu, 22°C. Detaylar için tıklayın.",
                modifier = Modifier.clickable { onCityClick("istanbul") },
            )
            ContentCard(
                title = "Ankara",
                description = "Bugün hava güneşli, 18°C. Detaylar için tıklayın.",
                modifier = Modifier.clickable { onCityClick("ankara") },
            )
            ContentCard(
                title = "İzmir",
                description = "Bugün hava açık, 25°C. Detaylar için tıklayın.",
                modifier = Modifier.clickable { onCityClick("izmir") },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HavaTheme {
        HomeScreen(onCityClick = {})
    }
}
