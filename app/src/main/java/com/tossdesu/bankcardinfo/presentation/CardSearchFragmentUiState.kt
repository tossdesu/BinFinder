package com.tossdesu.bankcardinfo.presentation

import com.tossdesu.bankcardinfo.domain.entity.CardBin
import com.tossdesu.bankcardinfo.domain.entity.CardInfo
import com.tossdesu.bankcardinfo.presentation.CardSearchFragmentUiState.*

/**
 * Sealed class for observe MainActivity UI state
 * [CardData] - successful response, receive info about cardInfo as [CardInfo] object
 * [BinSearchHistoryData] - response, receive livedata list of [CardBin] objects
 * [Loading] - showing progress bar, while loading data
 * [Error] - business logic errors, showing Snackbar without any buttons
 * [NoConnectionError] - IOException (No Internet connection), showing Snackbar with reload button
 * [NothingFoundNotification] - 404 HttpException handling, nothing was found
 * [FatalError] - showing alert dialog for HttpExceptions(except 404), Database Exceptions
 * and Unknown Exceptions
 */
sealed class CardSearchFragmentUiState {
    data class CardData(val cardInfo: CardInfo) : CardSearchFragmentUiState()
    data class BinSearchHistoryData(val cardBins: List<CardBin>) : CardSearchFragmentUiState()
    object Loading : CardSearchFragmentUiState()
    data class Error(val messageStringResource: Int) : CardSearchFragmentUiState()
    object NoConnectionError : CardSearchFragmentUiState()
    object NothingFoundNotification : CardSearchFragmentUiState()
    data class FatalError(val title: String, val message: String) : CardSearchFragmentUiState()
}