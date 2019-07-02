package com.marzec.ui.main

import com.marzec.di.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector



@Module
abstract class MainActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindConvertFragment() : ConverterFragment

    @FragmentScope
    @Binds
    abstract fun taskPresenter(presenter: ConverterPresenter): ConverterContract.Presenter
}