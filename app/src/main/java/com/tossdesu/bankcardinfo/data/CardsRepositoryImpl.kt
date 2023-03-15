package com.tossdesu.bankcardinfo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tossdesu.bankcardinfo.data.database.CardBinsDao
import com.tossdesu.bankcardinfo.data.database.entity.CardBinDbEntity
import com.tossdesu.bankcardinfo.data.network.ApiService
import com.tossdesu.bankcardinfo.data.network.SafeApiCall
import com.tossdesu.bankcardinfo.domain.CardsRepository
import com.tossdesu.bankcardinfo.domain.Resource
import com.tossdesu.bankcardinfo.domain.entity.CardInfo
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import javax.inject.Inject

class CardsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val cardBinsDao: CardBinsDao,
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
    override fun getSearchHistoryUseCase(): LiveData<List<CardBin>> =
        Transformations.map(cardBinsDao.getCardBins()) { cardBinDbEntities ->
            cardBinDbEntities.map { it.toCardBin() }
        }

    /**
     * Put searched cardInfo bin number as [CardBin] object into DB
     */
    override suspend fun saveBinUseCase(cardBin: CardBin) {
        cardBinsDao.saveCardBin(CardBinDbEntity.fromCardBin(cardBin))
    }
}