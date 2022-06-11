package com.lentatyka.focusstartproject

import com.lentatyka.focusstartproject.di.AppComponent
import com.lentatyka.focusstartproject.di.DaggerTestAppComponent

class TestApplication:FocusStartApplication() {
    override fun initializeComponent(): AppComponent {
        val component = DaggerTestAppComponent.factory().create(this)
        component.inject(this)
        return component
    }
}