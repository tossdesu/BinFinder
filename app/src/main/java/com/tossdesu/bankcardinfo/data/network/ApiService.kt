package com.tossdesu.bankcardinfo.data.network

import com.tossdesu.bankcardinfo.data.network.entity.CardResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{binNumber}")
    suspend fun getCardInfo(
        @Path("binNumber") binNumber: Int
    ): CardResponseBody
}