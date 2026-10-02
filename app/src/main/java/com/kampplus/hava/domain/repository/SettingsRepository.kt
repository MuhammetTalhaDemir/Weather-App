package com.kampplus.hava.domain.repository

import com.kampplus.hava.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

/**
 * Ayarlar veri erişim arayüzü.
 */
interface SettingsRepository {
    fun observeSettings(): Flow<UserSettings>
    suspend fun updateSettings(settings: UserSettings)
}
