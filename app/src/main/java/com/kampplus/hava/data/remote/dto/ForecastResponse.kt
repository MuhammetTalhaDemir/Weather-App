package com.kampplus.hava.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
    @SerialName("current") val current: CurrentWeatherDto? = null,
    @SerialName("hourly") val hourly: HourlyWeatherDto? = null,
    @SerialName("daily") val daily: DailyWeatherDto? = null,
)

@Serializable
data class CurrentWeatherDto(
    @SerialName("time") val time: String? = null,
    @SerialName("temperature_2m") val temperature: Double? = null,
    @SerialName("apparent_temperature") val apparentTemperature: Double? = null,
    @SerialName("relative_humidity_2m") val humidity: Int? = null,
    @SerialName("wind_speed_10m") val windSpeed: Double? = null,
    @SerialName("weather_code") val weatherCode: Int? = null,
    @SerialName("is_day") val isDay: Int? = null,
)

@Serializable
data class HourlyWeatherDto(
    @SerialName("time") val time: List<String> = emptyList(),
    @SerialName("temperature_2m") val temperature: List<Double> = emptyList(),
    @SerialName("weather_code") val weatherCode: List<Int> = emptyList(),
)

@Serializable
data class DailyWeatherDto(
    @SerialName("time") val time: List<String> = emptyList(),
    @SerialName("weather_code") val weatherCode: List<Int> = emptyList(),
    @SerialName("temperature_2m_max") val tempMax: List<Double> = emptyList(),
    @SerialName("temperature_2m_min") val tempMin: List<Double> = emptyList(),
    @SerialName("precipitation_probability_max") val precipitationProbMax: List<Int> = emptyList(),
)
