package com.ayslan.project_dm125.singleton

import android.content.Context
import androidx.preference.PreferenceManager

object PreferencesManager {
    private var showDailyNotification: Boolean = false

    fun updateDailyNotificationPref(context: Context) {
        showDailyNotification = PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean("change_data", false)
    }

    fun getDailyNotificationPref(): Boolean {
        return showDailyNotification
    }
}
