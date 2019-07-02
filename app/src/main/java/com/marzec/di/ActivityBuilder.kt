package com.marzec.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.marzec.ui.main.MainActivity
import com.marzec.ui.main.MainActivityModule

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

}