package com.kampplus.hava.domain.model

data class HourlyForecast(
    val time: String,
    val temperature: String,
    val weatherCode: Int?,
)

data class DailyForecast(
    val dayName: String,
    val precipitationProb: Int?,
    val weatherCode: Int?,
    val tempMin: String,
    val tempMax: String,
)

data class CityDetailData(
    val city: CityItem,
    val apparentTemp: String = "--°",
    val humidity: String = "--",
    val windSpeed: String = "--",
    val hourlyForecasts: List<HourlyForecast> = emptyList(),
    val dailyForecasts: List<DailyForecast> = emptyList(),
)
