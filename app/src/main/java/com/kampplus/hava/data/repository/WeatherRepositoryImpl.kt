package com.kampplus.hava.data.repository

import android.content.Context
import com.kampplus.hava.R
import com.kampplus.hava.core.common.dispatcher.IoDispatcher
import com.kampplus.hava.core.common.mapper.WeatherCodeMapper
import com.kampplus.hava.data.local.CityCatalog
import com.kampplus.hava.data.remote.api.OpenMeteoApi
import com.kampplus.hava.data.remote.dto.ForecastResponse
import com.kampplus.hava.domain.model.CityDetailData
import com.kampplus.hava.domain.model.CityItem
import com.kampplus.hava.domain.model.DailyForecast
import com.kampplus.hava.domain.model.HourlyForecast
import com.kampplus.hava.domain.model.ScreenData
import com.kampplus.hava.domain.repository.WeatherRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenMeteoApi,
    private val cityCatalog: CityCatalog,
    private val weatherCodeMapper: WeatherCodeMapper,
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WeatherRepository {

    override suspend fun getWeatherForCities(): ScreenData = withContext(ioDispatcher) {
        val allCities = cityCatalog.getAllCities()
        val chunkSize = 20
        val cityBatches = allCities.chunked(chunkSize)

        val updatedCities = mutableListOf<CityItem>()

        for (batch in cityBatches) {
            val responses: List<ForecastResponse> = if (batch.size == 1) {
                val singleCity = batch.first()
                val singleResponse = api.getSingleCityForecast(
                    latitude = singleCity.latitude,
                    longitude = singleCity.longitude,
                    timezone = "auto",
                )
                listOf(singleResponse)
            } else {
                val lats = batch.joinToString(",") { it.latitude.toString() }
                val lons = batch.joinToString(",") { it.longitude.toString() }
                api.getMultiCityForecast(latitude = lats, longitude = lons, timezone = "auto")
            }

            batch.forEachIndexed { index, city ->
                val forecast = responses.getOrNull(index)
                val temp = forecast?.current?.temperature?.let { "$it°C" } ?: "--°C"
                val code = forecast?.current?.weatherCode
                val isDay = forecast?.current?.isDay != 0
                val desc = weatherCodeMapper.mapCodeToDescription(code)

                updatedCities.add(
                    city.copy(
                        description = context.getString(R.string.temperature_format, temp, desc),
                        weatherCode = code,
                        isDay = isDay,
                    ),
                )
            }
        }

        ScreenData(
            cities = updatedCities,
        )
    }

    override suspend fun getWeatherForCity(cityId: String): CityDetailData? = withContext(ioDispatcher) {
        val allCities = cityCatalog.getAllCities()
        val city = allCities.find { it.id == cityId } ?: return@withContext null
        try {
            val forecast = api.getSingleCityDetailForecast(
                latitude = city.latitude,
                longitude = city.longitude,
                timezone = "auto",
            )
            val temp = forecast.current?.temperature?.let { "$it°C" } ?: "--°C"
            val code = forecast.current?.weatherCode
            val isDay = forecast.current?.isDay != 0
            val desc = weatherCodeMapper.mapCodeToDescription(code)

            val updatedCity = city.copy(
                description = context.getString(R.string.temperature_format, temp, desc),
                weatherCode = code,
                isDay = isDay,
            )

            parseCityDetailData(updatedCity, forecast)
        } catch (_: Exception) {
            CityDetailData(city = city)
        }
    }

    private fun parseCityDetailData(city: CityItem, forecast: ForecastResponse): CityDetailData {
        val current = forecast.current
        val apparentTemp = current?.apparentTemperature?.let { "${it.toInt()}°" } ?: "--°"
        val humidity = current?.humidity?.let { "%$it" } ?: "--"
        val windSpeed = current?.windSpeed?.let { "${it.toInt()} km/sa" } ?: "--"

        val currentIsoTime = current?.time.orEmpty()
        val currentDateStr = if (currentIsoTime.contains("T")) currentIsoTime.substringBefore("T") else ""
        val currentHourInt = try {
            if (currentIsoTime.contains("T")) {
                currentIsoTime.substringAfter("T").substringBefore(":").toInt()
            } else {
                0
            }
        } catch (_: Exception) {
            0
        }

        // Hourly forecast processing (current hour up to 24 hours)
        val hourlyList = mutableListOf<HourlyForecast>()
        val hourly = forecast.hourly
        if ((hourly != null) && hourly.time.isNotEmpty()) {
            var startIndex = hourly.time.indexOfFirst { t ->
                (currentDateStr.isEmpty() || t.startsWith(currentDateStr)) && try {
                    t.substringAfter("T").substringBefore(":").toInt() >= currentHourInt
                } catch (_: Exception) {
                    false
                }
            }
            if (startIndex < 0) startIndex = 0

            val count = 24.coerceAtMost(hourly.time.size - startIndex)

            for (i in startIndex until (startIndex + count)) {
                val timeRaw = hourly.time.getOrNull(i) ?: continue
                val hourValue = try {
                    timeRaw.substringAfter("T").substringBefore(":")
                } catch (_: Exception) {
                    "00"
                }
                val formattedTime = "$hourValue:00"
                val tempVal = hourly.temperature.getOrNull(i)?.let { "${it.toInt()}°" } ?: "--°"
                val hCode = hourly.weatherCode.getOrNull(i)

                hourlyList.add(
                    HourlyForecast(
                        time = formattedTime,
                        temperature = tempVal,
                        weatherCode = hCode,
                    ),
                )
            }
        }

        // Daily 7-day forecast processing
        val dailyList = mutableListOf<DailyForecast>()
        val daily = forecast.daily
        if ((daily != null) && daily.time.isNotEmpty()) {
            val count = daily.time.size.coerceAtMost(7)
            val currentLocale = Locale.getDefault()

            for (i in 0 until count) {
                val rawDate = daily.time[i]
                val dayName = if (i == 0) {
                    context.getString(R.string.day_today)
                } else {
                    try {
                        val dateString = if (rawDate.contains("T")) rawDate.substringBefore("T") else rawDate
                        val date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
                        date.dayOfWeek.getDisplayName(TextStyle.FULL, currentLocale)
                            .replaceFirstChar { it.uppercase() }
                    } catch (_: Exception) {
                        "Day ${i + 1}"
                    }
                }

                val pProb = daily.precipitationProbMax.getOrNull(i)
                val dCode = daily.weatherCode.getOrNull(i)
                val minTemp = daily.tempMin.getOrNull(i)?.let { "${it.toInt()}°" } ?: "--°"
                val maxTemp = daily.tempMax.getOrNull(i)?.let { "${it.toInt()}°" } ?: "--°"

                dailyList.add(
                    DailyForecast(
                        dayName = dayName,
                        precipitationProb = pProb,
                        weatherCode = dCode,
                        tempMin = minTemp,
                        tempMax = maxTemp,
                    ),
                )
            }
        }

        return CityDetailData(
            city = city,
            apparentTemp = apparentTemp,
            humidity = humidity,
            windSpeed = windSpeed,
            hourlyForecasts = hourlyList,
            dailyForecasts = dailyList,
        )
    }
}
