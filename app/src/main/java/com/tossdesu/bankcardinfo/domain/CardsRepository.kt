package com.tossdesu.bankcardinfo.domain

import com.tossdesu.bankcardinfo.domain.entity.CardInfo
import com.tossdesu.bankcardinfo.domain.entity.CardBin

interface CardsRepository {

    /**
     * Execute cardInfo info downloading request
     * @return Success|GenericError|NetworkError object of [Resource] sealed class
     */
    suspend fun getCardUseCase(binNumber: Int) : Resource<CardInfo>

    /**
     * Get all cardInfo bin numbers searched before from DB
     * @return list of [CardBin] objects
     */
//    suspend fun getSearchHistoryUseCase(): List<CardBin>

    /**
     * Put searched cardInfo bin number as [CardBin] object into DB
     */
//    suspend fun saveBinUseCase(cardBin: CardBin)
}