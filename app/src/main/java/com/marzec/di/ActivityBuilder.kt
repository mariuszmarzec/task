package com.marzec.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.marzec.ui.main.MainActivity
import com.marzec.ui.main.MainActivityModule

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    @ActivityScope
    abstract fun bindMainActivity(): MainActivity

}