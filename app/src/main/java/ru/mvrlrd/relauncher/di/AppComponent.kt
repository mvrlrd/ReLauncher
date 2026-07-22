package ru.mvrlrd.relauncher.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.mvrlrd.relauncher.terminal.Executor
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun executor(): Executor

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
