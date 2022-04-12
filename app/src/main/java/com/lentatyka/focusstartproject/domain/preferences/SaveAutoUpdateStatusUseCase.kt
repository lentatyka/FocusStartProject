package com.lentatyka.focusstartproject.domain.preferences

import javax.inject.Inject

class SaveAutoUpdateStatusUseCase @Inject constructor(private val preferences: PreferencesHelper) {
    operator fun invoke(isAccess: Boolean) = preferences.setAutoUpdateAccess(isAccess)
}