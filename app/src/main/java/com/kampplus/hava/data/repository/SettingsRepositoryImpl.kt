package com.kampplus.hava.data.repository

import com.kampplus.hava.core.common.dispatcher.IoDispatcher
import com.kampplus.hava.data.local.dao.SettingsDao
import com.kampplus.hava.data.local.entity.SettingsEntity
import com.kampplus.hava.domain.model.AppThemeMode
import com.kampplus.hava.domain.model.TemperatureUnit
import com.kampplus.hava.domain.model.UserSettings
import com.kampplus.hava.domain.model.WindSpeedUnit
import com.kampplus.hava.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDao: SettingsDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : SettingsRepository {

    override fun observeSettings(): Flow<UserSettings> {
        return settingsDao.observeSettings().map { entity ->
            if (entity == null) {
                UserSettings()
            } else {
                UserSettings(
                    tempUnit = try { TemperatureUnit.valueOf(entity.tempUnit) } catch (_: Exception) { TemperatureUnit.CELSIUS },
                    windUnit = try { WindSpeedUnit.valueOf(entity.windUnit) } catch (_: Exception) { WindSpeedUnit.KMH },
                    themeMode = try { AppThemeMode.valueOf(entity.themeMode) } catch (_: Exception) { AppThemeMode.SYSTEM },
                )
            }
        }
    }

    override suspend fun updateSettings(settings: UserSettings) = withContext(ioDispatcher) {
        settingsDao.saveSettings(
            SettingsEntity(
                id = 1,
                tempUnit = settings.tempUnit.name,
                windUnit = settings.windUnit.name,
                themeMode = settings.themeMode.name,
            ),
        )
    }
}
