package com.tossdesu.bankcardinfo.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tossdesu.bankcardinfo.data.database.entity.CardBinDbEntity

@Dao
interface CardBinsDao {

    /**
     * Put [CardBinDbEntity] object into DB
     */
    @Insert
    suspend fun saveCardBin(binDbEntity: CardBinDbEntity)

    /**
     * Get all card bins from DB
     * @return livedata list of [CardBinDbEntity] objects
     */
    @Query("SELECT * FROM ${CardBinDbEntity.NAME} ORDER BY id DESC")
    fun getCardBins(): LiveData<List<CardBinDbEntity>>
}