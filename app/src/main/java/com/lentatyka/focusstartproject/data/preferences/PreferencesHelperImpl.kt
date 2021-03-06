package com.lentatyka.focusstartproject.data.preferences

import android.content.Context
import com.lentatyka.focusstartproject.common.Constants.AUTOUPDATE
import com.lentatyka.focusstartproject.common.Constants.PREFERENCES
import com.lentatyka.focusstartproject.domain.preferences.PreferencesHelper
import javax.inject.Inject

class PreferencesHelperImpl @Inject constructor(private val context: Context): PreferencesHelper {
    override fun getAutoUpdateAccess(): Boolean {
        return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)?.getBoolean(
            AUTOUPDATE, false
        ) ?: false
    }

    override fun setAutoUpdateAccess(isAccess: Boolean) {
        // What about notify user if something wrong...? May be return false >>>
        val prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        prefs?.let {
            it.edit().putBoolean(AUTOUPDATE, isAccess)?.apply()
        }
    }
}