package com.kampplus.hava.core.common.mapper

import javax.inject.Inject

class WeatherCodeMapper @Inject constructor() {
    fun mapCodeToDescription(code: Int?): String = when (code) {
        0 -> "Açık ve Güneşli"
        1, 2, 3 -> "Parçalı Bulutlu"
        45, 48 -> "Sisli"
        51, 53, 55, 61, 63, 65, 80, 81, 82 -> "Sağanak Yağmurlu"
        71, 73, 75, 77, 85, 86 -> "Kar Yağışlı"
        95, 96, 99 -> "Gökgürültülü Fırtına"
        else -> "Hafif Bulutlu"
    }
}
