package com.marzec.ui.main

import com.marzec.di.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class MainActivityModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [ConverterFragmentModule::class])
    abstract fun bindConvertFragment() : ConverterFragment
}