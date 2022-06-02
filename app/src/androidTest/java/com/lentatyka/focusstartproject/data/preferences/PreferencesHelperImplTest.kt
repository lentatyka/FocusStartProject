package com.lentatyka.focusstartproject.data.preferences

import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test


class PreferencesHelperImplTest {

    private lateinit var pref: PreferencesHelperImpl

    @Before
    fun setUp() {
        pref = PreferencesHelperImpl(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun preferenceReturnDefaultFalseValue(){
        val actual =pref.getAutoUpdateAccess()
        assertThat(actual).isFalse()
    }

    @Test
    fun preferenceSaveTrueReturnTrue() {
        pref.setAutoUpdateAccess(true)
        val actual = pref.getAutoUpdateAccess()
        assertThat(actual).isTrue()
    }

    @Test
    fun preferenceSaveFalseReturnFalse() {
        pref.setAutoUpdateAccess(false)
        val actual = pref.getAutoUpdateAccess()
        assertThat(actual).isFalse()
    }
}