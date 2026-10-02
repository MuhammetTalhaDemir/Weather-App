package com.kampplus.hava.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kampplus.hava.R
import com.kampplus.hava.core.ui.theme.HavaTheme
import com.kampplus.hava.domain.model.AppThemeMode
import com.kampplus.hava.domain.model.TemperatureUnit
import com.kampplus.hava.domain.model.UserSettings
import com.kampplus.hava.domain.model.WindSpeedUnit

/**
 * Uygulama İçi Ayarlar Ekranı.
 *
 * @param userSettings Mevcut kullanıcı ayarları
 * @param onUpdateSettings Ayar güncellendiğinde çalışacak callback
 * @param onBackClick Geri butonuna basıldığında çalışacak callback
 * @param modifier Dışarıdan uygulanacak Modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userSettings: UserSettings,
    onUpdateSettings: (UserSettings) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.action_back),
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            // 1. Sıcaklık Birimi
            SettingsSectionCard(title = stringResource(id = R.string.settings_temp_unit)) {
                SettingsRadioButtonRow(
                    text = stringResource(id = R.string.settings_unit_celsius),
                    selected = userSettings.tempUnit == TemperatureUnit.CELSIUS,
                    onClick = { onUpdateSettings(userSettings.copy(tempUnit = TemperatureUnit.CELSIUS)) },
                )
                SettingsRadioButtonRow(
                    text = stringResource(id = R.string.settings_unit_fahrenheit),
                    selected = userSettings.tempUnit == TemperatureUnit.FAHRENHEIT,
                    onClick = { onUpdateSettings(userSettings.copy(tempUnit = TemperatureUnit.FAHRENHEIT)) },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Rüzgar Birimi
            SettingsSectionCard(title = stringResource(id = R.string.settings_wind_unit)) {
                SettingsRadioButtonRow(
                    text = stringResource(id = R.string.settings_unit_kmh),
                    selected = userSettings.windUnit == WindSpeedUnit.KMH,
                    onClick = { onUpdateSettings(userSettings.copy(windUnit = WindSpeedUnit.KMH)) },
                )
                SettingsRadioButtonRow(
                    text = stringResource(id = R.string.settings_unit_mph),
                    selected = userSettings.windUnit == WindSpeedUnit.MPH,
                    onClick = { onUpdateSettings(userSettings.copy(windUnit = WindSpeedUnit.MPH)) },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Tema Seçimi
            SettingsSectionCard(title = stringResource(id = R.string.settings_theme_title)) {
                SettingsRadioButtonRow(
                    text = stringResource(id = R.string.settings_theme_system),
                    selected = userSettings.themeMode == AppThemeMode.SYSTEM,
                    onClick = { onUpdateSettings(userSettings.copy(themeMode = AppThemeMode.SYSTEM)) },
                )
                SettingsRadioButtonRow(
                    text = stringResource(id = R.string.settings_theme_light),
                    selected = userSettings.themeMode == AppThemeMode.LIGHT,
                    onClick = { onUpdateSettings(userSettings.copy(themeMode = AppThemeMode.LIGHT)) },
                )
                SettingsRadioButtonRow(
                    text = stringResource(id = R.string.settings_theme_dark),
                    selected = userSettings.themeMode == AppThemeMode.DARK,
                    onClick = { onUpdateSettings(userSettings.copy(themeMode = AppThemeMode.DARK)) },
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun SettingsRadioButtonRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    HavaTheme {
        SettingsScreen(
            userSettings = UserSettings(),
            onUpdateSettings = {},
            onBackClick = {},
        )
    }
}
