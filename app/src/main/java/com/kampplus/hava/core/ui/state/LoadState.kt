package com.kampplus.hava.core.ui.state

import com.kampplus.hava.domain.model.ScreenData

/**
 * Dört ana ekran durumunu temsil eden sealed interface.
 */
sealed interface LoadState {
    data object Loading : LoadState
    data class Content(val data: ScreenData) : LoadState
    data object Empty : LoadState
    data class Error(val message: String) : LoadState
}
