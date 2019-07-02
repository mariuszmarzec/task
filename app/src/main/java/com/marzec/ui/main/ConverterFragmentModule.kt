package com.marzec.ui.main

import com.marzec.di.FragmentScope
import dagger.Binds
import dagger.Module

@Module
abstract class ConverterFragmentModule {

    @FragmentScope
    @Binds
    abstract fun taskPresenter(presenter: ConverterPresenter): ConverterContract.Presenter
}
