package com.kampplus.hava.presentation.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Ekran durumlarını ve kullanıcı aksiyonlarını yöneten ortak ViewModel.
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /**
     * Şehir seçim aksiyonunu işler ve durum günceller.
     *
     * @param cityId Seçilen şehrin kimliği
     */
    fun selectCity(cityId: String) {
        val selectedCity = _uiState.value.cityList.find { it.id == cityId }
        val name = selectedCity?.name ?: cityId.replaceFirstChar { it.uppercase() }
        val description = selectedCity?.description ?: "$name şehri için detaylı hava durumu bilgisi."

        _uiState.update { currentState ->
            currentState.copy(
                selectedCityId = cityId,
                selectedCityName = name,
                weatherDescription = description,
            )
        }
    }
}
