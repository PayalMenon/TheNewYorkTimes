package com.example.newyorktimes.dagger.module

import androidx.lifecycle.ViewModel
import com.example.newyorktimes.dagger.ViewModelKey
import com.example.newyorktimes.viewmodel.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindStarWarsViewModel(viewModel: SearchViewModel): ViewModel
}