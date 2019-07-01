package marzec.pl.daggerexample.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import marzec.pl.daggerexample.ui.hello.HelloActivity
import marzec.pl.daggerexample.ui.hello.HelloBuilder
import marzec.pl.daggerexample.ui.hello.HelloModule
import marzec.pl.daggerexample.ui.main.MainActivity
import marzec.pl.daggerexample.ui.main.MainActivityModule

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [HelloModule::class, HelloBuilder::class])
    @ActivityScope
    abstract fun bindHelloActivity(): HelloActivity

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    @ActivityScope
    abstract fun bindMainActivity(): MainActivity

}