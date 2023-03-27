package com.tossdesu.bankcardinfo.domain

import androidx.lifecycle.LiveData
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import com.tossdesu.bankcardinfo.domain.entity.CardInfo

interface CardsRepository {

    /**
     * Execute cardInfo info downloading request
     * @return Success|Error|GenericError|NetworkError object of [Result] sealed class
     */
    suspend fun getCardUseCase(binString: String) : Result<CardInfo>

    /**
     * Get all cardInfo bin numbers searched before from DB
     * @return livedata list of [CardBin] objects
     */
    fun getSearchHistoryUseCase(): LiveData<List<CardBin>>

    /**
     * Put searched cardInfo bin number as [CardBin] object into DB
     */
    suspend fun saveBinUseCase(cardBin: CardBin)
}