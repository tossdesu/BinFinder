package com.tossdesu.bankcardinfo.domain.usecase

import com.tossdesu.bankcardinfo.R
import com.tossdesu.bankcardinfo.domain.CardsRepository
import com.tossdesu.bankcardinfo.domain.Result
import com.tossdesu.bankcardinfo.domain.entity.CardInfo
import javax.inject.Inject

class GetCardUseCase @Inject constructor(
    private val repository: CardsRepository
) {
    companion object {
        const val BIN_SIZE = 6
    }

    suspend operator fun invoke(binString: String): Result<CardInfo> {
        return if (binString.length == BIN_SIZE) {
            repository.getCardUseCase(binString)
        } else {
            Result.Error(R.string.bin_validate_error)
        }
    }
}