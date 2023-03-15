package com.tossdesu.bankcardinfo.domain.usecase

import com.tossdesu.bankcardinfo.domain.CardsRepository
import com.tossdesu.bankcardinfo.domain.Resource
import com.tossdesu.bankcardinfo.domain.entity.CardInfo
import javax.inject.Inject

class GetCardUseCase @Inject constructor(
    private val repository: CardsRepository
) {

    suspend operator fun invoke(binNumber: Int): Resource<CardInfo> {
        return repository.getCardUseCase(binNumber)
    }
}