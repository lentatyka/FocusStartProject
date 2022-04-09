package com.lentatyka.focusstartproject.di

import com.lentatyka.focusstartproject.data.preferences.PreferencesHelperImpl
import com.lentatyka.focusstartproject.domain.preferences.PreferencesHelper
import dagger.Binds
import dagger.Module

@Module
abstract class PreferencesModule {

    @Binds
    abstract fun bindPreferences(prefs: PreferencesHelperImpl): PreferencesHelper
}