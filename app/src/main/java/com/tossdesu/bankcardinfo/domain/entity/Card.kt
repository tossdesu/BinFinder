package com.tossdesu.bankcardinfo.domain.entity

data class Card(
    val bankName: String,
    val bankUrl: String,
    val bankPhone: String,
    val bankCity: String,
    val countryShortName: String,
    val countryName:String,
    val currency: String,
    val latitude: Int,
    val longitude: Int,
    val cardScheme: String,
    val cardType: String,
    val cardBrand: String,
    val isCardPrepaid: Boolean,
    val cardNumberLength: Int,
    val isCardLuhn: Boolean
)
