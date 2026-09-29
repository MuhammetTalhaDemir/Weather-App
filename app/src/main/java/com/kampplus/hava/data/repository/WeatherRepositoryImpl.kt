package com.kampplus.hava.data.repository

import com.kampplus.hava.core.common.dispatcher.IoDispatcher
import com.kampplus.hava.core.common.mapper.WeatherCodeMapper
import com.kampplus.hava.data.remote.api.OpenMeteoApi
import com.kampplus.hava.domain.model.CityItem
import com.kampplus.hava.domain.model.ScreenData
import com.kampplus.hava.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenMeteoApi,
    private val weatherCodeMapper: WeatherCodeMapper,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WeatherRepository {

    private val cityMetadata = listOf(
        CityItem("istanbul", "İstanbul", "", 41.0082, 28.9784),
        CityItem("ankara", "Ankara", "", 39.9334, 32.8597),
        CityItem("izmir", "İzmir", "", 38.4237, 27.1428),
    )

    override suspend fun getWeatherForCities(): ScreenData = withContext(ioDispatcher) {
        val lats = cityMetadata.joinToString(",") { it.latitude.toString() }
        val lons = cityMetadata.joinToString(",") { it.longitude.toString() }

        val responses = api.getMultiCityForecast(latitude = lats, longitude = lons)

        val cityList = cityMetadata.mapIndexed { index, city ->
            val forecast = responses.getOrNull(index)
            val temp = forecast?.current?.temperature?.let { "$it°C" } ?: "--°C"
            val desc = weatherCodeMapper.mapCodeToDescription(forecast?.current?.weatherCode)
            city.copy(description = "Sıcaklık: $temp, $desc")
        }

        ScreenData(cities = cityList)
    }

    override suspend fun getWeatherForCity(cityId: String): CityItem? = withContext(ioDispatcher) {
        val screenData = getWeatherForCities()
        screenData.cities.find { it.id == cityId }
    }
}
