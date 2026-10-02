package com.kampplus.hava.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Kullanıcı ayarlarının veritabanı varlık sınıfı.
 */
@Entity(tableName = "user_settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    val tempUnit: String = "CELSIUS",
    val windUnit: String = "KMH",
    val themeMode: String = "SYSTEM",
)
