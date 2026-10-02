package com.kampplus.hava.data.remote.api

import com.kampplus.hava.data.remote.dto.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {
    @GET("forecast")
    suspend fun getSingleCityForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,weather_code,is_day",
        @Query("timezone") timezone: String = "auto",
    ): ForecastResponse

    @GET("forecast")
    suspend fun getSingleCityDetailForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,apparent_temperature,relative_humidity_2m,wind_speed_10m,weather_code,is_day",
        @Query("hourly") hourly: String = "temperature_2m,weather_code",
        @Query("daily") daily: String = "weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max",
        @Query("timezone") timezone: String = "auto",
    ): ForecastResponse

    @GET("forecast")
    suspend fun getMultiCityForecast(
        @Query("latitude", encoded = true) latitude: String,
        @Query("longitude", encoded = true) longitude: String,
        @Query("current") current: String = "temperature_2m,weather_code,is_day",
        @Query("timezone") timezone: String = "auto",
    ): List<ForecastResponse>
}
