package com.lentatyka.focusstartproject

import android.app.Application
import com.lentatyka.focusstartproject.di.AppComponent
import com.lentatyka.focusstartproject.di.DaggerAppComponent

open class FocusStartApplication:Application() {

    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    open fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}