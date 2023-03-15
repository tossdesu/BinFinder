package com.tossdesu.bankcardinfo.di

import android.app.Application
import com.tossdesu.bankcardinfo.data.CardsRepositoryImpl
import com.tossdesu.bankcardinfo.data.database.AppDatabase
import com.tossdesu.bankcardinfo.data.database.CardBinsDao
import com.tossdesu.bankcardinfo.data.network.ApiFactory
import com.tossdesu.bankcardinfo.data.network.ApiService
import com.tossdesu.bankcardinfo.domain.CardsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindCardsRepositoryImpl(impl: CardsRepositoryImpl): CardsRepository

    companion object {

        @Provides
        fun provideCardBinsDao(
            application: Application
        ): CardBinsDao {
            return AppDatabase.getInstance(application).cardBinsDao()
        }

        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}