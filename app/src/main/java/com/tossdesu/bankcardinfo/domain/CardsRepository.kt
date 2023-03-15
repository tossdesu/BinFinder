package com.tossdesu.bankcardinfo.domain

import androidx.lifecycle.LiveData
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import com.tossdesu.bankcardinfo.domain.entity.CardInfo

interface CardsRepository {

    /**
     * Execute cardInfo info downloading request
     * @return Success|GenericError|NetworkError object of [Resource] sealed class
     */
    suspend fun getCardUseCase(binNumber: Int) : Resource<CardInfo>

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