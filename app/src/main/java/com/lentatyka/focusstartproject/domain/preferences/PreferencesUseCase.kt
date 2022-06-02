package com.lentatyka.focusstartproject.domain.preferences

import javax.inject.Inject

class PreferencesUseCase @Inject constructor(
    private val preferences: PreferencesHelper
) {

    fun getAutoUpdateAccess(): Boolean = preferences.getAutoUpdateAccess()

    fun saveAutoUpdateAccess(isAccess: Boolean) = preferences.setAutoUpdateAccess(isAccess)
}