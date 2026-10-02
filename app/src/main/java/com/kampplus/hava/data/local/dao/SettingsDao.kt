package com.kampplus.hava.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kampplus.hava.data.local.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Kullanıcı ayarları Room DAO arayüzü.
 */
@Dao
interface SettingsDao {

    @Query("SELECT * FROM user_settings WHERE id = 1")
    fun observeSettings(): Flow<SettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: SettingsEntity)
}
