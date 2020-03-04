package com.example.newyorktimes.dagger.module

import com.example.newyorktimes.dagger.ViewModelBuilder
import com.example.newyorktimes.ui.ArticleDetailsFragment
import com.example.newyorktimes.ui.SearchActivity
import com.example.newyorktimes.ui.ArticleListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    abstract fun startActivity(): SearchActivity

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    abstract fun searchFragment(): ArticleListFragment

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    abstract fun articleDetailsFragment(): ArticleDetailsFragment
}