package com.kampplus.hava.core.database.di

import android.content.Context
import androidx.room.Room
import com.kampplus.hava.core.database.HavaDatabase
import com.kampplus.hava.data.local.dao.FavoriteCityDao
import com.kampplus.hava.data.local.dao.SettingsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHavaDatabase(
        @ApplicationContext context: Context,
    ): HavaDatabase {
        return Room.databaseBuilder(
            context,
            HavaDatabase::class.java,
            "hava.db",
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideFavoriteCityDao(
        database: HavaDatabase,
    ): FavoriteCityDao = database.favoriteCityDao()

    @Provides
    @Singleton
    fun provideSettingsDao(
        database: HavaDatabase,
    ): SettingsDao = database.settingsDao()
}
