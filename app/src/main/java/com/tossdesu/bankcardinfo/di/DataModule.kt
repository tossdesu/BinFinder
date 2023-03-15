package com.tossdesu.bankcardinfo.di

import com.tossdesu.bankcardinfo.data.CardsRepositoryImpl
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

//        @Provides
//        fun provideAccountDao(
//            application: Application
//        ): RecipesDao {
//            return AppDatabase.getInstance(application).recipesDao()
//        }

        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}