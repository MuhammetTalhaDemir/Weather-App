package com.kampplus.hava.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kampplus.hava.core.ui.state.LoadState
import com.kampplus.hava.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Ekran durumlarını ve API isteklerini yöneten ViewModel.
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private val _loadState = MutableStateFlow<LoadState>(LoadState.Loading)
    val loadState: StateFlow<LoadState> = _loadState.asStateFlow()

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
                _loadState.value = LoadState.Error(
                    message = t.localizedMessage ?: "Ağ veya veri ayrıştırma hatası oluştu.",
                )
            }
        }
    }
}
