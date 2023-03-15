package com.tossdesu.bankcardinfo.data.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory {

    private const val BASE_URL = "https://lookup.binlist.net/"

    private val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val moshi = Moshi.Builder().build()
    private val moshiConverterFactory = MoshiConverterFactory.create(moshi)

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(moshiConverterFactory)
        .client(client)
        .baseUrl(BASE_URL)
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}