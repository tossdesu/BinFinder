package com.tossdesu.bankcardinfo.data.network.entity

data class CardCountryDto(
    val alpha2: String,
    val name: String,
    val currency: String,
    val latitude: Int,
    val longitude: Int
)
