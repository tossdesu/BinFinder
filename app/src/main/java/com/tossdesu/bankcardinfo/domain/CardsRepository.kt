package com.tossdesu.bankcardinfo.domain

import com.tossdesu.bankcardinfo.domain.entity.Card
import com.tossdesu.bankcardinfo.domain.entity.CardBin

interface CardsRepository {

    /**
     * Execute card info downloading request
     * @return Success|GenericError|NetworkError object of [Resource] sealed class
     */
    suspend fun getCardUseCase(binNumber: Int) : Card

    /**
     * Get all card bin numbers searched before from DB
     * @return list of [CardBin] objects
     */
    suspend fun getSearchHistoryUseCase(): List<CardBin>

    /**
     * Put searched card bin number as [CardBin] object into DB
     */
    suspend fun saveBinUseCase(cardBin: CardBin)
}