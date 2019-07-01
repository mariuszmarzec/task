package marzec.pl.daggerexample

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import marzec.pl.daggerexample.di.DaggerAppComponent

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }


}