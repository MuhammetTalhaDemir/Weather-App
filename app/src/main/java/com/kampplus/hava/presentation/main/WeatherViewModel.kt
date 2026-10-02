package com.kampplus.hava.presentation.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kampplus.hava.R
import com.kampplus.hava.core.ui.state.LoadState
import com.kampplus.hava.domain.model.CityDetailData
import com.kampplus.hava.domain.model.ScreenData
import com.kampplus.hava.domain.model.TemperatureUnit
import com.kampplus.hava.domain.model.UserSettings
import com.kampplus.hava.domain.model.WindSpeedUnit
import com.kampplus.hava.domain.repository.FavoriteCityRepository
import com.kampplus.hava.domain.repository.SettingsRepository
import com.kampplus.hava.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Ekran durumlarını, ayarları ve API isteklerini yöneten ViewModel.
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val favoriteCityRepository: FavoriteCityRepository,
    private val settingsRepository: SettingsRepository,
    @param:ApplicationContext private val context: Context,
) : ViewModel() {

    private val _loadState = MutableStateFlow<LoadState>(LoadState.Loading)
    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val userSettings: StateFlow<UserSettings> = settingsRepository
        .observeSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UserSettings(),
        )

    private val favoriteCityIds: StateFlow<List<String>> = favoriteCityRepository
        .observeFavoriteCityIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList(),
        )

    val loadState: StateFlow<LoadState> = combine(_loadState, favoriteCityIds, userSettings) { state, favIds, settings ->
        if (state is LoadState.Content) {
            val favSet = favIds.toSet()
            val formattedCities = state.data.cities.map { city ->
                city.copy(
                    description = formatCityDescription(city.description, settings.tempUnit),
                    isFavorite = favSet.contains(city.id),
                )
            }
            val favoriteCityItems = favIds.mapNotNull { id ->
                formattedCities.find { it.id == id }
            }
            val formattedDetails = state.data.cityDetails.mapValues { (_, detail) ->
                formatCityDetail(detail, settings)
            }
            LoadState.Content(
                ScreenData(
                    cities = formattedCities,
                    favoriteCities = favoriteCityItems,
                    cityDetails = formattedDetails,
                ),
            )
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = LoadState.Loading,
    )

    init {
        loadData()
    }

    /**
     * API çağrısını başlatır ve yükleme durumunu günceller.
     */
    fun loadData() {
        viewModelScope.launch {
            _loadState.value = LoadState.Loading
            try {
                val screenData = weatherRepository.getWeatherForCities()
                if (screenData.cities.isEmpty()) {
                    _loadState.value = LoadState.Empty
                } else {
                    _loadState.value = LoadState.Content(screenData)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (t: Throwable) {
                try {
                    Log.e("WeatherError", "Hata mesajı: ${t.message}", t)
                } catch (_: Throwable) {
                    // Unit test ortamında Log.e mock'lanmadığında hataya sebep olmaması için.
                }

                val errorMessage = when (t) {
                    is UnknownHostException,
                    is ConnectException,
                    is SocketTimeoutException,
                    is IOException,
                    -> context.getString(R.string.error_no_internet)

                    is HttpException -> context.getString(R.string.error_server)

                    else -> t.localizedMessage ?: context.getString(R.string.error_unknown)
                }

                _loadState.value = LoadState.Error(message = errorMessage)
            }
        }
    }

    /**
     * Seçilen şehir için detaylı hava verilerini çeker ve durumu günceller.
     */
    fun loadCityDetail(cityId: String) {
        viewModelScope.launch {
            try {
                val currentLoadState = _loadState.value
                if ((currentLoadState is LoadState.Content) && currentLoadState.data.cityDetails.containsKey(cityId)) {
                    return@launch
                }

                val detailData = weatherRepository.getWeatherForCity(cityId) ?: return@launch
                val state = _loadState.value
                if (state is LoadState.Content) {
                    val currentDetails = state.data.cityDetails.toMutableMap()
                    currentDetails[cityId] = detailData
                    _loadState.value = LoadState.Content(
                        state.data.copy(cityDetails = currentDetails),
                    )
                }
            } catch (t: Throwable) {
                try {
                    Log.e("WeatherError", "Şehir detay hatası: ${t.message}", t)
                } catch (_: Throwable) {
                    // Test ortamı için
                }
            }
        }
    }

    fun updateUserSettings(settings: UserSettings) {
        viewModelScope.launch {
            settingsRepository.updateSettings(settings)
        }
    }

    fun toggleFavorite(cityId: String) {
        viewModelScope.launch {
            favoriteCityRepository.toggleFavorite(cityId)
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.update { query }
    }

    private fun formatCityDescription(description: String, unit: TemperatureUnit): String {
        val tempPrefixTR = "Sıcaklık: "
        val tempPrefixEN = "Temp: "
        val prefix = if (description.contains(tempPrefixTR)) {
            tempPrefixTR
        } else if (description.contains(tempPrefixEN)) {
            tempPrefixEN
        } else {
            return description
        }

        if (unit == TemperatureUnit.CELSIUS) return description
        val tempPart = description.substringAfter(prefix).substringBefore(",")
        val tempNum = tempPart.replace("°C", "").trim().toDoubleOrNull() ?: return description
        val fahrenheit = ((tempNum * 9.0) / 5.0) + 32.0
        return description.replace(tempPart, "${fahrenheit.toInt()}°F")
    }

    private fun formatCityDetail(detail: CityDetailData, settings: UserSettings): CityDetailData {
        val formattedApparent = formatTempString(detail.apparentTemp, settings.tempUnit)
        val formattedWind = formatWindString(detail.windSpeed, settings.windUnit)
        val formattedHourly = detail.hourlyForecasts.map { h ->
            h.copy(temperature = formatTempString(h.temperature, settings.tempUnit))
        }
        val formattedDaily = detail.dailyForecasts.map { d ->
            d.copy(
                tempMin = formatTempString(d.tempMin, settings.tempUnit),
                tempMax = formatTempString(d.tempMax, settings.tempUnit),
            )
        }

        return detail.copy(
            apparentTemp = formattedApparent,
            windSpeed = formattedWind,
            hourlyForecasts = formattedHourly,
            dailyForecasts = formattedDaily,
        )
    }

    private fun formatTempString(tempStr: String, unit: TemperatureUnit): String {
        if (unit == TemperatureUnit.CELSIUS) return tempStr
        val num = tempStr.replace("°C", "").replace("°", "").trim().toDoubleOrNull() ?: return tempStr
        val fahrenheit = ((num * 9.0) / 5.0) + 32.0
        return "${fahrenheit.toInt()}°F"
    }

    private fun formatWindString(windStr: String, unit: WindSpeedUnit): String {
        if (unit == WindSpeedUnit.KMH) return windStr
        val num = windStr.replace("km/sa", "").trim().toDoubleOrNull() ?: return windStr
        val mph = num * 0.621371
        return "${mph.toInt()} mph"
    }
}
