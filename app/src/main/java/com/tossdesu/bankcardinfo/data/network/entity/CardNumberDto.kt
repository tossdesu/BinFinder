package com.tossdesu.bankcardinfo.data.network.entity

data class CardNumberDto(
    val length: Int? = null,
    val luhn: Boolean? = null
)
