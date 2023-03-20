package com.tossdesu.bankcardinfo.data.network

import com.tossdesu.bankcardinfo.data.network.entity.CardResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{binString}")
    suspend fun getCardInfo(
        @Path("binString") binString: String
    ): CardResponseBody
}