package com.kampplus.hava.domain.repository

import com.kampplus.hava.domain.model.CityItem
import com.kampplus.hava.domain.model.ScreenData

/**
 * Hava durumu veri erişim arayüzü.
 */
interface WeatherRepository {
    suspend fun getWeatherForCities(): ScreenData
    suspend fun getWeatherForCity(cityId: String): CityItem?
}
