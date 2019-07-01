package marzec.pl.daggerexample.di

import dagger.*
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import marzec.pl.daggerexample.App
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityBuilder::class])
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<App>
}

