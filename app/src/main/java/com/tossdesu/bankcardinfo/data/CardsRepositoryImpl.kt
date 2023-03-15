package com.tossdesu.bankcardinfo.data

import com.tossdesu.bankcardinfo.data.network.ApiService
import com.tossdesu.bankcardinfo.data.network.SafeApiCall
import com.tossdesu.bankcardinfo.domain.CardsRepository
import com.tossdesu.bankcardinfo.domain.Resource
import com.tossdesu.bankcardinfo.domain.entity.CardInfo
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import javax.inject.Inject

class CardsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val safeApiCall: SafeApiCall
) : CardsRepository {

    /**
     * Execute cardInfo info downloading request
     * @return Success|Exception object of [Resource] sealed class
     */
    override suspend fun getCardUseCase(binNumber: Int): Resource<CardInfo> = safeApiCall.run {
        apiService.getCardInfo(binNumber).toCard()
    }

    /**
     * Get all cardInfo bin numbers searched before from DB
     * @return list of [CardBin] objects
     */
//    override suspend fun getSearchHistoryUseCase(): List<CardBin> {
//        TODO("Not yet implemented")
//    }

    /**
     * Put searched cardInfo bin number as [CardBin] object into DB
     */
//    override suspend fun saveBinUseCase(cardBin: CardBin) {
//        TODO("Not yet implemented")
//    }
}