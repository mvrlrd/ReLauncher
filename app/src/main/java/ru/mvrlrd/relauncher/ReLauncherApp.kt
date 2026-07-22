package ru.mvrlrd.relauncher

import android.app.Application
import ru.mvrlrd.relauncher.di.AppComponent
import ru.mvrlrd.relauncher.di.DaggerAppComponent

class ReLauncherApp : Application() {

    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.factory().create(this)
    }
}
