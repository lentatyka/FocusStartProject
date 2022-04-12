package com.lentatyka.focusstartproject.domain.preferences

import javax.inject.Inject

class LoadAutoUpdateStatusUseCase @Inject constructor(private val preferences: PreferencesHelper) {
    operator fun invoke():Boolean = preferences.getAutoUpdateAccess()
}