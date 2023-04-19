package com.tossdesu.bankcardinfo.di

import androidx.lifecycle.ViewModel
import com.tossdesu.bankcardinfo.presentation.CardSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(CardSearchViewModel::class)
    @Binds
    fun bindCardSearchViewModel(viewModel: CardSearchViewModel): ViewModel
}