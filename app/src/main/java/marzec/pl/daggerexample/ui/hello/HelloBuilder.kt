package marzec.pl.daggerexample.ui.hello

import dagger.Module
import dagger.android.ContributesAndroidInjector
import marzec.pl.daggerexample.di.FragmentScope

@Module
abstract class HelloBuilder {

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun bindHelloFragment() : HelloFragment
}