package com.kampplus.hava.presentation.main

import com.kampplus.hava.core.ui.state.LoadState
import com.kampplus.hava.domain.model.CityItem
import com.kampplus.hava.domain.model.ScreenData
import com.kampplus.hava.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadData emits Content state when repository returns cities`() = runTest {
        val fakeRepo = object : WeatherRepository {
            override suspend fun getWeatherForCities(): ScreenData = ScreenData(
                cities = listOf(
                    CityItem("istanbul", "İstanbul", "Sıcaklık: 22°C", 41.0, 28.9),
                ),
            )

            override suspend fun getWeatherForCity(cityId: String): CityItem =
                CityItem("istanbul", "İstanbul", "Sıcaklık: 22°C", 41.0, 28.9)
        }

        val viewModel = WeatherViewModel(fakeRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.loadState.value
        assertTrue(state is LoadState.Content)
        assertEquals(1, (state as LoadState.Content).data.cities.size)
    }

    @Test
    fun `loadData emits Empty state when repository returns empty cities`() = runTest {
        val fakeRepo = object : WeatherRepository {
            override suspend fun getWeatherForCities(): ScreenData = ScreenData(cities = emptyList())
            override suspend fun getWeatherForCity(cityId: String): CityItem? = null
        }

        val viewModel = WeatherViewModel(fakeRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.loadState.value
        assertTrue(state is LoadState.Empty)
    }

    @Test
    fun `loadData emits Error state when repository throws Exception`() = runTest {
        val fakeRepo = object : WeatherRepository {
            override suspend fun getWeatherForCities(): ScreenData {
                throw RuntimeException("Ağ hatası")
            }

            override suspend fun getWeatherForCity(cityId: String): CityItem? = null
        }

        val viewModel = WeatherViewModel(fakeRepo)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.loadState.value
        assertTrue(state is LoadState.Error)
        assertEquals("Ağ hatası", (state as LoadState.Error).message)
    }
}
