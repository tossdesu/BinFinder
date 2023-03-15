package com.tossdesu.bankcardinfo.domain.usecase

import javax.inject.Inject

class ValidateBinNumberUseCase @Inject constructor() {

    companion object {
        const val BIN_SIZE = 6
    }

    operator fun invoke(binString: String?): Int {
        val input = binString ?: ""
        return if (input.length == BIN_SIZE) {
            try { input.toInt() } catch (e: NumberFormatException) { 0 }
        } else {
            0
        }
    }
}