package com.kampplus.hava.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.core.ui.state.LoadState
import com.kampplus.hava.core.ui.theme.HavaTheme
import com.kampplus.hava.domain.model.CityItem
import com.kampplus.hava.domain.model.ScreenData
import com.kampplus.hava.presentation.components.ContentCard

/**
 * Detay ekranı Composable bileşeni.
 *
 * @param cityId Detayı gösterilecek şehrin kimliği
 * @param loadState Yükleme durumu
 * @param onBackClick Geri butonuna basıldığında çalışacak callback
 * @param onRetry Hata durumunda tekrar denemek için callback
 * @param modifier Dışarıdan uygulanacak Modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    cityId: String,
    loadState: LoadState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedCity = (loadState as? LoadState.Content)?.data?.cities?.find { it.id == cityId }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "${selectedCity?.name ?: cityId.replaceFirstChar { it.uppercase() }} Detayı") },
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
                    if (selectedCity != null) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            ContentCard(
                                title = selectedCity.name,
                                description = selectedCity.description,
                            )
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
    HavaTheme {
        DetailScreen(
            cityId = "istanbul",
            loadState = LoadState.Content(
                ScreenData(
                    cities = listOf(CityItem("istanbul", "İstanbul", "Sıcaklık: 22°C, Parçalı Bulutlu", 41.0, 28.9)),
                ),
            ),
            onBackClick = {},
            onRetry = {},
        )
    }
}
