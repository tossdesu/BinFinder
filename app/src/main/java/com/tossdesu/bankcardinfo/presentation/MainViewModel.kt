package com.tossdesu.bankcardinfo.presentation

import androidx.lifecycle.*
import com.tossdesu.bankcardinfo.domain.Resource
import com.tossdesu.bankcardinfo.domain.Resource.Exception.Cause.*
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import com.tossdesu.bankcardinfo.domain.entity.CardInfo
import com.tossdesu.bankcardinfo.domain.usecase.GetCardUseCase
import com.tossdesu.bankcardinfo.domain.usecase.GetSearchHistoryUseCase
import com.tossdesu.bankcardinfo.domain.usecase.SaveBinUseCase
import com.tossdesu.bankcardinfo.domain.usecase.ValidateBinNumberUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCardUseCase: GetCardUseCase,
    private val saveBinUseCase: SaveBinUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val validateBinNumberUseCase: ValidateBinNumberUseCase
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

    fun getCardInfo(binString: String) {
        searchJob?.let {
            if (it.isActive)
                return
        }
        searchJob = viewModelScope.launch {
            val isValidate = validateBinNumberUseCase(binString)
            if (isValidate) {
                _uiState.value = MainActivityUiState.Loading
                val cardResponse = getCardUseCase(binString)
                handleResponse(cardResponse, binString)
            } else {
                _uiState.value = MainActivityUiState.ValidateError
            }
        }
    }

    fun getCardInfoFromHistory(binString: String) {
        searchJob?.let {
            if (it.isActive)
                return
        }
        searchJob = viewModelScope.launch {
            _uiState.value = MainActivityUiState.Loading
            val cardResponse = getCardUseCase(binString)
            handleResponse(cardResponse)
        }
    }

    private suspend fun handleResponse(
        resource: Resource<CardInfo>,
        binString: String? = null
    ) {
        if (resource is Resource.Success) {
            _uiState.value = MainActivityUiState.CardData(resource.data)
            binString?.let { bin ->
                saveCardBin(CardBin(bin = bin))
            }
        } else if (resource is Resource.Exception) {
            when(resource.cause) {
                is NoConnection -> {
                    _uiState.value = MainActivityUiState.NoConnectionError
                }
                is HttpResponseNothingFound -> {
                    _uiState.value = MainActivityUiState.NothingFoundNotification
                }
                is HttpException -> {
                    _uiState.value = MainActivityUiState.FatalError(
                        "Network Error",
                        "Code: ${resource.cause.code} \nMessage: ${resource.cause.message}"
                    )
                }
                is Unknown -> {
                    _uiState.value = MainActivityUiState.FatalError(
                        "Unknown Error",
                        resource.cause.message
                    )
                }
            }
        }
    }

    private suspend fun saveCardBin(bin: CardBin) {
        saveBinUseCase(bin)
    }
}