package com.lentatyka.focusstartproject.domain.preferences

interface PreferencesHelper {
    fun getAutoUpdateAccess(): Boolean
    fun setAutoUpdateAccess(isAccess: Boolean)
}