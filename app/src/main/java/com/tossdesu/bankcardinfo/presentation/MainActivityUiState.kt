package com.tossdesu.bankcardinfo.presentation

import com.tossdesu.bankcardinfo.domain.entity.Card
import com.tossdesu.bankcardinfo.presentation.MainActivityUiState.*

/**
 * Sealed class for observe MainActivity UI state
 * [Data] - successful response, receive info about card as [Card] object
 * [Loading] - showing progress bar, while loading data
 * [FatalError] - showing alert dialog for HttpExceptions(except 404, 400) and Unknown Exceptions
 * [ErrorWithReload] - IOException (No Internet connection), showing Snackbar with reload button
 * [Error] - business logic errors, showing Snackbar without any buttons
 * [NothingFoundNotification] - 404, 400 HttpException handling, nothing was found
 */
sealed class MainActivityUiState {
    data class Data(val card: Card) : MainActivityUiState()
    object Loading : MainActivityUiState()
    object FatalError : MainActivityUiState()
    object ErrorWithReload : MainActivityUiState()
    object Error : MainActivityUiState()
    object NothingFoundNotification : MainActivityUiState()
}