package com.tossdesu.bankcardinfo.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tossdesu.bankcardinfo.domain.Result
import com.tossdesu.bankcardinfo.domain.Result.Exception.Cause.*
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import com.tossdesu.bankcardinfo.domain.usecase.GetCardUseCase
import com.tossdesu.bankcardinfo.domain.usecase.GetSearchHistoryUseCase
import com.tossdesu.bankcardinfo.domain.usecase.SaveBinUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class CardSearchViewModel @Inject constructor(
    private val getCardUseCase: GetCardUseCase,
    private val saveBinUseCase: SaveBinUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<CardSearchFragmentUiState>()
    val uiState: LiveData<CardSearchFragmentUiState>
        get() = _uiState

    private var searchJob: Job? = null
    private var historyJob: Job? = null

    fun getHistory() {
        historyJob?.let {
            if (it.isActive)
                return
        }
        historyJob = viewModelScope.launch {
            val result = getSearchHistoryUseCase()
            if (result is Result.Success) {
                _uiState.value = CardSearchFragmentUiState.BinSearchHistoryData(result.data)
            } else if (result is Result.Exception) {
                handleException(result)
            }
        }
    }

    fun getCardInfo(binString: String, isHistoryQuery: Boolean = false) {
        searchJob?.let {
            if (it.isActive)
                return
        }
        searchJob = viewModelScope.launch {

            _uiState.value = CardSearchFragmentUiState.Loading
            val result = getCardUseCase(binString)

            if (result is Result.Success) {
                _uiState.value = CardSearchFragmentUiState.CardData(result.data)
                if (!isHistoryQuery) {
                    saveCardBin(CardBin(bin = binString))
                }

            // Business logic error handling
            } else if (result is Result.Error) {
                _uiState.value = CardSearchFragmentUiState.Error(result.messageStringResource)

            // Exceptions handling
            } else if (result is Result.Exception) {
                handleException(result)
            }
        }
    }

    private suspend fun saveCardBin(bin: CardBin) {
        val result = saveBinUseCase(bin)
        if (result is Result.Exception)
            handleException(result)
    }

    private fun handleException(e: Result.Exception) {
        when (e.cause) {
            is NoConnection -> {
                _uiState.value = CardSearchFragmentUiState.NoConnectionError
            }
            is HttpResponseNothingFound -> {
                _uiState.value = CardSearchFragmentUiState.NothingFoundNotification
            }
            is HttpException -> {
                _uiState.value = CardSearchFragmentUiState.FatalError(
                    "Network Error",
                    "Code: ${e.cause.code} \nMessage: ${e.cause.message}"
                )
            }
            is DatabaseException -> {
                _uiState.value = CardSearchFragmentUiState.FatalError(
                    "Database Error",
                    e.cause.message
                )
            }
            is Unknown -> {
                _uiState.value = CardSearchFragmentUiState.FatalError(
                    "Unknown Error",
                    e.cause.message
                )
            }
        }
    }
}