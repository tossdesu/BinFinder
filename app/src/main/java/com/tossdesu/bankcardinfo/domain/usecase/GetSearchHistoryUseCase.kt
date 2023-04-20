package com.tossdesu.bankcardinfo.domain.usecase

import com.tossdesu.bankcardinfo.domain.CardsRepository
import com.tossdesu.bankcardinfo.domain.Result
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(
    private val repository: CardsRepository
) {

    suspend operator fun invoke(): Result<List<CardBin>> {
        return repository.getSearchHistoryUseCase()
    }
}