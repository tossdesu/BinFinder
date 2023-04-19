package com.tossdesu.bankcardinfo.domain.usecase

import androidx.lifecycle.LiveData
import com.tossdesu.bankcardinfo.domain.CardsRepository
import com.tossdesu.bankcardinfo.domain.Result
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import javax.inject.Inject

class GetSearchHistoryUseCase@Inject constructor(
    private val repository: CardsRepository
) {

    operator fun invoke(): LiveData<Result<List<CardBin>>> {
        return repository.getSearchHistoryUseCase()
    }
}