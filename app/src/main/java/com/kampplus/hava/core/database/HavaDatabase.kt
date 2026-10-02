package com.kampplus.hava.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kampplus.hava.data.local.dao.FavoriteCityDao
import com.kampplus.hava.data.local.dao.SettingsDao
import com.kampplus.hava.data.local.entity.FavoriteCityEntity
import com.kampplus.hava.data.local.entity.SettingsEntity

/**
 * Yerel Room Veritabanı.
 */
@Database(
    entities = [FavoriteCityEntity::class, SettingsEntity::class],
    version = 3,
    exportSchema = true,
)
abstract class HavaDatabase : RoomDatabase() {
    abstract fun favoriteCityDao(): FavoriteCityDao
    abstract fun settingsDao(): SettingsDao
}
