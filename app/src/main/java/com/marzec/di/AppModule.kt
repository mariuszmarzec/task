package com.marzec.di

import android.content.Context
import dagger.Binds
import dagger.Module
import com.marzec.App

@Module
interface AppModule {
    @Binds
    fun bindContext(application: App): Context
}