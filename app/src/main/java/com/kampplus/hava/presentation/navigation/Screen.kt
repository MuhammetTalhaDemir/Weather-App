package com.kampplus.hava.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * Uygulama içi ekran rotalarını temsil eden sealed interface.
 */
sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data class Detail(val cityId: String) : Screen

    @Serializable
    data object Settings : Screen
}
