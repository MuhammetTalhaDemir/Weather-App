package com.kampplus.hava.presentation.main

import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {

    @Test
    fun `initial uiState contains default istanbul values`() {
        val viewModel = MainViewModel()

        val state = viewModel.uiState.value
        assertEquals("istanbul", state.selectedCityId)
        assertEquals("İstanbul", state.selectedCityName)
        assertEquals("Bugün hava parçalı bulutlu, 22°C.", state.weatherDescription)
    }

    @Test
    fun `selectCity updates uiState correctly for existing city`() {
        val viewModel = MainViewModel()

        viewModel.selectCity("ankara")

        val state = viewModel.uiState.value
        assertEquals("ankara", state.selectedCityId)
        assertEquals("Ankara", state.selectedCityName)
        assertEquals("Bugün hava güneşli, 18°C.", state.weatherDescription)
    }

    @Test
    fun `selectCity updates uiState correctly for unknown city`() {
        val viewModel = MainViewModel()

        viewModel.selectCity("bursa")

        val state = viewModel.uiState.value
        assertEquals("bursa", state.selectedCityId)
        assertEquals("Bursa", state.selectedCityName)
        assertEquals("Bursa şehri için detaylı hava durumu bilgisi.", state.weatherDescription)
    }
}
