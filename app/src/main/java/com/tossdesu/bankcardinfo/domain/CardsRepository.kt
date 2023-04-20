package com.tossdesu.bankcardinfo.domain

import com.tossdesu.bankcardinfo.domain.entity.CardBin
import com.tossdesu.bankcardinfo.domain.entity.CardInfo

interface CardsRepository {

    /**
     * Execute cardInfo info downloading request
     * @return Success|Exception object of [Result] sealed class
     */
    suspend fun getCardUseCase(binString: String): Result<CardInfo>

    /**
     * Get all cardInfo bin numbers searched before from DB
     * @return livedata Success|DatabaseException object of [Result] sealed class
     */
    suspend fun getSearchHistoryUseCase(): Result<List<CardBin>>

    /**
     * Put searched cardInfo bin number as [CardBin] object into DB
     *  @return Success|DatabaseException object of [Result] sealed class
     */
    suspend fun saveBinUseCase(cardBin: CardBin): Result<Unit>
}