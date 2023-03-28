package com.tossdesu.bankcardinfo.domain.usecase

import com.tossdesu.bankcardinfo.domain.CardsRepository
import com.tossdesu.bankcardinfo.domain.Result
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import javax.inject.Inject

class SaveBinUseCase @Inject constructor(
    private val repository: CardsRepository
) {

    suspend operator fun invoke(bin: CardBin): Result<Unit> {
        return repository.saveBinUseCase(bin)
    }
}