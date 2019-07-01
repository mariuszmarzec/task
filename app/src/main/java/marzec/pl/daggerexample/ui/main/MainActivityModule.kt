package marzec.pl.daggerexample.ui.main

import android.content.Context
import dagger.Module
import dagger.Provides
import marzec.pl.daggerexample.R
import marzec.pl.daggerexample.di.ActivityScope

@Module
class MainActivityModule {
    @Provides
    @ActivityScope
    fun provideAppName(context: Context): String = context.getString(R.string.app_name)
}