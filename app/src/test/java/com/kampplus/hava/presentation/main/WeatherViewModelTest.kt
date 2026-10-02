package com.kampplus.hava.presentation.main

import android.content.Context
import com.kampplus.hava.R
import com.kampplus.hava.core.ui.state.LoadState
import com.kampplus.hava.domain.model.CityDetailData
import com.kampplus.hava.domain.model.CityItem
import com.kampplus.hava.domain.model.ScreenData
import com.kampplus.hava.domain.model.UserSettings
import com.kampplus.hava.domain.repository.FavoriteCityRepository
import com.kampplus.hava.domain.repository.SettingsRepository
import com.kampplus.hava.domain.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockContext = mockk<Context>()

    private val fakeFavoriteRepo = object : FavoriteCityRepository {
        private val favs = MutableStateFlow<List<String>>(emptyList())
        override fun observeFavoriteCityIds(): Flow<List<String>> = favs
        override suspend fun toggleFavorite(cityId: String) {
            favs.value = if (favs.value.contains(cityId)) favs.value - cityId else favs.value + cityId
        }
    }

    private val fakeSettingsRepo = object : SettingsRepository {
        private val settings = MutableStateFlow(UserSettings())
        override fun observeSettings(): Flow<UserSettings> = settings
        override suspend fun updateSettings(settings: UserSettings) {
            this.settings.value = settings
        }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { mockContext.getString(R.string.error_no_internet) } returns "İnternet Bağlantısı Bulunamadı"
        every { mockContext.getString(R.string.error_server) } returns "Sunucu hatası veya servis ulaşılamıyor"
        every { mockContext.getString(R.string.error_unknown) } returns "Bilinmeyen hata"
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadData emits Content state when repository returns cities`() = runTest {
        val sampleCity = CityItem("istanbul", "İstanbul", "Sıcaklık: 22°C", 41.0, 28.9)
        val fakeRepo = object : WeatherRepository {
            override suspend fun getWeatherForCities(): ScreenData = ScreenData(
                cities = listOf(sampleCity),
            )

            override suspend fun getWeatherForCity(cityId: String): CityDetailData =
                CityDetailData(city = sampleCity)
        }

        val viewModel = WeatherViewModel(fakeRepo, fakeFavoriteRepo, fakeSettingsRepo, mockContext)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.loadState.value
        assertTrue(state is LoadState.Content)
        assertEquals(1, (state as LoadState.Content).data.cities.size)
    }

    @Test
    fun `loadData emits Empty state when repository returns empty cities`() = runTest {
        val fakeRepo = object : WeatherRepository {
            override suspend fun getWeatherForCities(): ScreenData = ScreenData(cities = emptyList())
            override suspend fun getWeatherForCity(cityId: String): CityDetailData? = null
        }

        val viewModel = WeatherViewModel(fakeRepo, fakeFavoriteRepo, fakeSettingsRepo, mockContext)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.loadState.value
        assertTrue(state is LoadState.Empty)
    }

    @Test
    fun `loadData emits internet baglantisi bulunamadi on network exception`() = runTest {
        val fakeRepo = object : WeatherRepository {
            override suspend fun getWeatherForCities(): ScreenData {
                throw UnknownHostException("Host not found")
            }

            override suspend fun getWeatherForCity(cityId: String): CityDetailData? = null
        }

        val viewModel = WeatherViewModel(fakeRepo, fakeFavoriteRepo, fakeSettingsRepo, mockContext)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.loadState.value
        assertTrue(state is LoadState.Error)
        assertEquals("İnternet Bağlantısı Bulunamadı", (state as LoadState.Error).message)
    }

    @Test
    fun `loadData emits sunucu hatasi on http exception`() = runTest {
        val fakeRepo = object : WeatherRepository {
            override suspend fun getWeatherForCities(): ScreenData {
                throw HttpException(Response.error<Any>(500, "".toResponseBody()))
            }

            override suspend fun getWeatherForCity(cityId: String): CityDetailData? = null
        }

        val viewModel = WeatherViewModel(fakeRepo, fakeFavoriteRepo, fakeSettingsRepo, mockContext)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.loadState.value
        assertTrue(state is LoadState.Error)
        assertEquals("Sunucu hatası veya servis ulaşılamıyor", (state as LoadState.Error).message)
    }
}
