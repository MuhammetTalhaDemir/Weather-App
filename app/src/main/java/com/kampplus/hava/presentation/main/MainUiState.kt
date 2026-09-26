package com.kampplus.hava.presentation.main

/**
 * Ana ekran ve detay ekranı durumlarını temsil eden data class.
 */
data class MainUiState(
    val selectedCityId: String = "istanbul",
    val selectedCityName: String = "İstanbul",
    val weatherDescription: String = "Bugün hava parçalı bulutlu, 22°C.",
    val cityList: List<CityItem> = listOf(
        CityItem(id = "istanbul", name = "İstanbul", description = "Bugün hava parçalı bulutlu, 22°C."),
        CityItem(id = "ankara", name = "Ankara", description = "Bugün hava güneşli, 18°C."),
        CityItem(id = "izmir", name = "İzmir", description = "Bugün hava açık, 25°C."),
    ),
    val isLoading: Boolean = false,
)

/**
 * Şehir bilgisini tutan veri modeli.
 */
data class CityItem(
    val id: String,
    val name: String,
    val description: String,
)
