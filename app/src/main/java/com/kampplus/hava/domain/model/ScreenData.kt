package com.kampplus.hava.domain.model

/**
 * Ekran verilerini tutan data class.
 */
data class ScreenData(
    val cities: List<CityItem> = emptyList(),
    val favoriteCities: List<CityItem> = emptyList(),
    val cityDetails: Map<String, CityDetailData> = emptyMap(),
)
