package com.tossdesu.bankcardinfo.di

import androidx.lifecycle.ViewModel
import com.tossdesu.bankcardinfo.presentation.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}