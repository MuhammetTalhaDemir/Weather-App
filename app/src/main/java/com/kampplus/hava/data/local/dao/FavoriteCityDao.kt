package com.kampplus.hava.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kampplus.hava.data.local.entity.FavoriteCityEntity
import kotlinx.coroutines.flow.Flow

/**
 * Favori şehirler Room DAO arayüzü.
 */
@Dao
interface FavoriteCityDao {

    @Query("SELECT cityId FROM favorite_cities ORDER BY addedAt ASC")
    fun observeFavoriteCityIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteCity(favoriteCity: FavoriteCityEntity)

    @Query("DELETE FROM favorite_cities WHERE cityId = :cityId")
    suspend fun deleteFavoriteCity(cityId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_cities WHERE cityId = :cityId)")
    suspend fun isFavorite(cityId: String): Boolean
}
