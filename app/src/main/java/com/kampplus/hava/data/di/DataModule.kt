package com.kampplus.hava.data.di

import com.kampplus.hava.data.repository.FavoriteCityRepositoryImpl
import com.kampplus.hava.data.repository.SettingsRepositoryImpl
import com.kampplus.hava.data.repository.WeatherRepositoryImpl
import com.kampplus.hava.domain.repository.FavoriteCityRepository
import com.kampplus.hava.domain.repository.SettingsRepository
import com.kampplus.hava.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl,
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteCityRepository(
        impl: FavoriteCityRepositoryImpl,
    ): FavoriteCityRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl,
    ): SettingsRepository
}
