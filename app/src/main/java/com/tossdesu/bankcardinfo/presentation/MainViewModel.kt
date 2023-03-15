package com.tossdesu.bankcardinfo.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tossdesu.bankcardinfo.domain.Resource
import com.tossdesu.bankcardinfo.domain.Resource.Exception.Cause.*
import com.tossdesu.bankcardinfo.domain.entity.CardInfo
import com.tossdesu.bankcardinfo.domain.usecase.GetCardUseCase
import com.tossdesu.bankcardinfo.domain.usecase.ValidateBinNumberUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCardUseCase: GetCardUseCase,
    private val validateBinNumberUseCase: ValidateBinNumberUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<MainActivityUiState>()
    val uiState: LiveData<MainActivityUiState>
        get() = _uiState

    fun getCardInfo(binString: String?) {
        viewModelScope.launch {
            val binNumber = validateBinNumberUseCase(binString)
            if (binNumber == 0) {
                _uiState.value = MainActivityUiState.ValidateError
            } else {
                _uiState.value = MainActivityUiState.Loading
                val response = getCardUseCase(binNumber)
                handleResponse(response)
            }
        }
    }

    private fun handleResponse(resource: Resource<CardInfo>) {
        if (resource is Resource.Success) {
            _uiState.value = MainActivityUiState.CardData(resource.data)
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


}