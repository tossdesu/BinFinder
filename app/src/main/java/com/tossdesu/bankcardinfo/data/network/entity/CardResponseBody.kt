package com.tossdesu.bankcardinfo.data.network.entity

import com.tossdesu.bankcardinfo.domain.entity.CardInfo

/**
 * Response body for `GET /{binNumber}` HTTP-request.
 *
 * JSON example:
 * ```
 * {
 *   "number": {
 *      "length": 16
 *      "luhn": true
 *   },
 *   "scheme": "visa",
 *   "type": "debit",
 *   "brand": "Visa/Dankort",
 *   "prepaid": false,
 *   "country": {
 *       "numeric": "208", (not in use)
 *       "alpha2": "DK",
 *       "name": "Denmark",
 *       "emoji": "🇩🇰", (not in use)
 *       "currency": "DKK",
 *       "latitude": 56,
 *       "longitude": 10
 *   },
 *   "bank": {
 *       "name": "Jyske Bank",
 *       "url": "www.jyskebank.dk",
 *       "phone": "+4589893300",
 *       "city": "Hjørring"
 *   }
 * }
 * ```
 */
data class CardResponseBody(
    val number: CardNumberDto,
    val scheme: String,
    val type: String? = null,
    val brand: String? = null,
    val prepaid: Boolean? = null,
    val country: CardCountryDto,
    val bank: CardBankDto? = null
) {

    /**
     * Convert this entity into in-app [CardInfo] instance.
     */
    fun toCard(): CardInfo = CardInfo(
        bankName = bank?.name,
        bankUrl = bank?.url,
        bankPhone = bank?.phone,
        bankCity = bank?.city,
        countryAlpha2 = country.alpha2,
        countryName = country.name,
        currency = country.currency,
        latitude = country.latitude.toString(),
        longitude = country.longitude.toString(),
        cardScheme = scheme,
        cardType = type,
        cardBrand = brand,
        isCardPrepaid = prepaid,
        cardNumberLength = number.length,
        isCardLuhn = number.luhn
    )
}
