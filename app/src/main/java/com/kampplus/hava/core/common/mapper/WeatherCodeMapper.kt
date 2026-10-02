package com.kampplus.hava.core.common.mapper

import android.content.Context
import com.kampplus.hava.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeatherCodeMapper @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    fun mapCodeToDescription(code: Int?): String = when (code) {
        0, 1 -> context.getString(R.string.weather_sunny)
        2 -> context.getString(R.string.weather_partly_cloudy)
        3 -> context.getString(R.string.weather_overcast)
        45, 48 -> context.getString(R.string.weather_foggy)
        51, 53, 55, 56, 57 -> context.getString(R.string.weather_drizzle)
        61, 63, 65 -> context.getString(R.string.weather_rainy)
        80, 81, 82 -> context.getString(R.string.weather_heavy_rain)
        66, 67, 89, 90 -> context.getString(R.string.weather_hail)
        71, 73, 75, 77, 85, 86 -> context.getString(R.string.weather_snowy)
        95, 96, 99 -> context.getString(R.string.weather_stormy)
        else -> context.getString(R.string.weather_sunny)
    }
}
