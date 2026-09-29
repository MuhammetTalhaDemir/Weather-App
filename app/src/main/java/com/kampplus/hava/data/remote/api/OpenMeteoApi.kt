package com.kampplus.hava.data.remote.api

import com.kampplus.hava.data.remote.dto.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {
    @GET("forecast")
    suspend fun getMultiCityForecast(
        @Query("latitude", encoded = true) latitude: String,
        @Query("longitude", encoded = true) longitude: String,
        @Query("current") current: String = "temperature_2m,weather_code",
        @Query("timezone") timezone: String = "auto",
    ): List<ForecastResponse>
}
