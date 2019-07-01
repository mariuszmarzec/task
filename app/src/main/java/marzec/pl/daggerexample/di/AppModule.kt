package marzec.pl.daggerexample.di

import android.content.Context
import dagger.Binds
import dagger.Module
import marzec.pl.daggerexample.App

@Module
interface AppModule {
    @Binds
    fun bindContext(application: App): Context
}