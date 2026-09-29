package com.kampplus.hava.domain.model

/**
 * Şehir hava durumu verisini temsil eden domain modeli.
 */
data class CityItem(
    val id: String,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
)
