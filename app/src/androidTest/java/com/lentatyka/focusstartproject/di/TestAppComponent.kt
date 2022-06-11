package com.lentatyka.focusstartproject.di

import android.content.Context
import com.lentatyka.focusstartproject.TestApplication
import com.lentatyka.focusstartproject.presentation.MainActivityTest
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FakeNetworkModule::class, ViewModelModule::class, PreferencesModule::class])
interface TestAppComponent:AppComponent {
    fun inject(test: MainActivityTest)
    fun inject(app: TestApplication)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context):TestAppComponent
    }
}