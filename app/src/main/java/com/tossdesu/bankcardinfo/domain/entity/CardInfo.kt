package com.tossdesu.bankcardinfo.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardInfo(
    val bankName: String?,
    val bankUrl: String?,
    val bankPhone: String?,
    val bankCity: String?,
    val countryAlpha2: String,
    val countryName:String,
    val currency: String,
    val latitude: Int,
    val longitude: Int,
    val cardScheme: String,
    val cardType: String?,
    val cardBrand: String?,
    val isCardPrepaid: Boolean?,
    val cardNumberLength: Int?,
    val isCardLuhn: Boolean?
) : Parcelable