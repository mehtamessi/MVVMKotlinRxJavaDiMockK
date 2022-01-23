package com.mukesh.assignment_android.utils

import android.content.Context
import android.preference.PreferenceManager


class SharedPreferenceHelper(context: Context) {
    private val PREF_API_KEY = "api_key"

    private val pref = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    fun getStoredApiKey() = pref.getString(PREF_API_KEY, null)

    fun saveAPIKey(key: String) {
        pref.edit().putString(PREF_API_KEY, key).apply()
    }
}