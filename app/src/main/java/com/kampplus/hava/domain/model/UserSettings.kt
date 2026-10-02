package com.kampplus.hava.domain.model

enum class TemperatureUnit { CELSIUS, FAHRENHEIT }
enum class WindSpeedUnit { KMH, MPH }
enum class AppThemeMode { SYSTEM, LIGHT, DARK }

/**
 * Kullanıcı ayarları veri modeli.
 */
data class UserSettings(
    val tempUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val windUnit: WindSpeedUnit = WindSpeedUnit.KMH,
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
)
