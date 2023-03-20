package com.tossdesu.bankcardinfo.domain.usecase

import javax.inject.Inject

class ValidateBinNumberUseCase @Inject constructor() {

    companion object {
        const val BIN_SIZE = 6
    }

    operator fun invoke(binString: String): Boolean = binString.length == BIN_SIZE
}