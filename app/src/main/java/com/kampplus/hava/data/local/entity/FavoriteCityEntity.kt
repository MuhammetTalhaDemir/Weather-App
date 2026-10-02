package com.kampplus.hava.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Favori şehirlerin veritabanı varlık (entity) sınıfı.
 */
@Entity(tableName = "favorite_cities")
data class FavoriteCityEntity(
    @PrimaryKey val cityId: String,
    val addedAt: Long = System.currentTimeMillis(),
)
