package com.lentatyka.focusstartproject.di

import com.lentatyka.focusstartproject.presentation.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}