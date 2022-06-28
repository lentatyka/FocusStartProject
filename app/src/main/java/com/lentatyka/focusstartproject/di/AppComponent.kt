package com.lentatyka.focusstartproject.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.lentatyka.focusstartproject.presentation.MainActivity
import com.lentatyka.focusstartproject.presentation.MainViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class, PreferencesModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context):AppComponent
    }

    fun viewModelsFactory():ViewModelProvider.Factory
}