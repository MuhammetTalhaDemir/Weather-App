package com.kampplus.hava.data.repository

import com.kampplus.hava.core.common.dispatcher.IoDispatcher
import com.kampplus.hava.data.local.dao.FavoriteCityDao
import com.kampplus.hava.data.local.entity.FavoriteCityEntity
import com.kampplus.hava.domain.repository.FavoriteCityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteCityRepositoryImpl @Inject constructor(
    private val favoriteCityDao: FavoriteCityDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FavoriteCityRepository {

    override fun observeFavoriteCityIds(): Flow<List<String>> {
        return favoriteCityDao.observeFavoriteCityIds()
    }

    override suspend fun toggleFavorite(cityId: String) = withContext(ioDispatcher) {
        val isFav = favoriteCityDao.isFavorite(cityId)
        if (isFav) {
            favoriteCityDao.deleteFavoriteCity(cityId)
        } else {
            favoriteCityDao.insertFavoriteCity(
                FavoriteCityEntity(
                    cityId = cityId,
                    addedAt = System.currentTimeMillis(),
                ),
            )
        }
    }
}
