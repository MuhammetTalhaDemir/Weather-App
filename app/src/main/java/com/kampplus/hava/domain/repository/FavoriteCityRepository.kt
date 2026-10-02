package com.kampplus.hava.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Favori şehir veri erişim arayüzü.
 */
interface FavoriteCityRepository {
    fun observeFavoriteCityIds(): Flow<List<String>>
    suspend fun toggleFavorite(cityId: String)
}
