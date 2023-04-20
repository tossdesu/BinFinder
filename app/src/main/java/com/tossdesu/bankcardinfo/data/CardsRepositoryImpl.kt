package com.tossdesu.bankcardinfo.data

import com.tossdesu.bankcardinfo.data.database.CardBinsDao
import com.tossdesu.bankcardinfo.data.database.entity.CardBinDbEntity
import com.tossdesu.bankcardinfo.data.network.ApiService
import com.tossdesu.bankcardinfo.data.network.SafeApiCall
import com.tossdesu.bankcardinfo.domain.CardsRepository
import com.tossdesu.bankcardinfo.domain.Result
import com.tossdesu.bankcardinfo.domain.Result.Exception.Cause
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import com.tossdesu.bankcardinfo.domain.entity.CardInfo
import javax.inject.Inject

class CardsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val cardBinsDao: CardBinsDao,
    private val safeApiCall: SafeApiCall
) : CardsRepository {

    /**
     * Execute cardInfo info downloading request
     * @return Success|Exception object of [Result] sealed class
     */
    override suspend fun getCardUseCase(binString: String): Result<CardInfo> = safeApiCall.run {
        apiService.getCardInfo(binString).toCard()
    }

    /**
     * Get all cardInfo bin numbers searched before from DB
     * @return Success|DatabaseException object of [Result] sealed class
     */
    override suspend fun getSearchHistoryUseCase(): Result<List<CardBin>> {
        return try {
            val cardBinsDbEntitiesList = cardBinsDao.getCardBins()
            val cardBinsList = cardBinsDbEntitiesList.map { it.toCardBin() }
            Result.Success(cardBinsList)
        } catch (e: Exception) {
            Result.Exception(
                Cause.DatabaseException(
                    e.message ?: "Database Error while getting search history"
                )
            )
        }
    }

    /**
     * Put searched cardInfo bin number as [CardBin] object into DB
     *  @return Success|DatabaseException object of [Result] sealed class
     */
    override suspend fun saveBinUseCase(cardBin: CardBin): Result<Unit> {
        return try {
            cardBinsDao.saveCardBin(CardBinDbEntity.fromCardBin(cardBin))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Exception(
                Cause.DatabaseException(
                    e.message ?: "Database Error while saving searched bin number"
                )
            )
        }
    }
}