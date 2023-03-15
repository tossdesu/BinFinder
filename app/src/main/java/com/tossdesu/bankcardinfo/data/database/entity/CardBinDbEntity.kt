package com.tossdesu.bankcardinfo.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tossdesu.bankcardinfo.data.database.entity.CardBinDbEntity.Companion.NAME
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * Card DB Entity
 *
 * JSON example:
 * ```
 * {
 *   "id": 1,
 *   "bin": "123456"
 * }
 * ```
 */
@Entity(
    tableName = NAME
)
data class CardBinDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val bin: String
) {

    /**
     * Convert this entity into in-app [CardBin] instance.
     */
    fun toCardBin() = CardBin(
        id = id,
        bin = bin
    )


    companion object {

        const val NAME = "card_bins"

        /**
         * Convert this entity into DB [CardBinDbEntity] instance.
         */
        fun fromCardBin(cardBin: CardBin) = CardBinDbEntity(
            id = cardBin.id,
            bin = cardBin.bin
        )
    }
}