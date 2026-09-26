package com.kampplus.hava.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
 * Detay ekranı Composable bileşeni.
 *
 * @param uiState Ekran durumu
 * @param onBackClick Geri butonuna basıldığında çalışacak callback
 * @param modifier Dışarıdan uygulanacak Modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: MainUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "${uiState.selectedCityName} Detayı") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
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
                .padding(16.dp),
        ) {
            ContentCard(
                title = uiState.selectedCityName,
                description = uiState.weatherDescription,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    HavaTheme {
        DetailScreen(
            uiState = MainUiState(
                selectedCityId = "istanbul",
                selectedCityName = "İstanbul",
                weatherDescription = "Bugün hava parçalı bulutlu, 22°C.",
            ),
            onBackClick = {},
        )
    }
}
