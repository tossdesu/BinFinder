package com.tossdesu.bankcardinfo.presentation

import androidx.lifecycle.*
import com.tossdesu.bankcardinfo.domain.Result
import com.tossdesu.bankcardinfo.domain.Result.Exception.Cause.*
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import com.tossdesu.bankcardinfo.domain.usecase.GetCardUseCase
import com.tossdesu.bankcardinfo.domain.usecase.GetSearchHistoryUseCase
import com.tossdesu.bankcardinfo.domain.usecase.SaveBinUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCardUseCase: GetCardUseCase,
    private val saveBinUseCase: SaveBinUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<MainActivityUiState>()
    val uiState: LiveData<MainActivityUiState>
        get() = _uiState

    private val binsHistory by lazy {
        getSearchHistoryUseCase()
    }

    private val binSearchUpdated by lazy {
        Observer<List<CardBin>> {
            _uiState.value = MainActivityUiState.BinSearchHistoryData(it)
        }
    }

    private var searchJob: Job? = null

    init {
        getHistory()
    }

    private fun getHistory() {
        binsHistory.observeForever(binSearchUpdated)
    }

    override fun onCleared() {
        binsHistory.removeObserver(binSearchUpdated)
        super.onCleared()
    }

    fun getCardInfo(binString: String, isHistoryQuery: Boolean = false) {
        searchJob?.let {
            if (it.isActive)
                return
        }
        searchJob = viewModelScope.launch {

            _uiState.value = MainActivityUiState.Loading
            val result = getCardUseCase(binString)

            if (result is Result.Success) {
                _uiState.value = MainActivityUiState.CardData(result.data)
                if (!isHistoryQuery) {
                    saveCardBin(CardBin(bin = binString))
                }

            } else if (result is Result.Error) {
                _uiState.value = MainActivityUiState.Error(result.messageStringResource)

            } else if (result is Result.Exception) {
                when (result.cause) {
                    is NoConnection -> {
                        _uiState.value = MainActivityUiState.NoConnectionError
                    }
                    is HttpResponseNothingFound -> {
                        _uiState.value = MainActivityUiState.NothingFoundNotification
                    }
                    is HttpException -> {
                        _uiState.value = MainActivityUiState.FatalError(
                            "Network Error",
                            "Code: ${result.cause.code} \nMessage: ${result.cause.message}"
                        )
                    }
                    is Unknown -> {
                        _uiState.value = MainActivityUiState.FatalError(
                            "Unknown Error",
                            result.cause.message
                        )
                    }
                }
            }
        }
    }

    private suspend fun saveCardBin(bin: CardBin) {
        saveBinUseCase(bin)
    }
}