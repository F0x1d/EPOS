package com.f0x1d.epos.utils

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(ctx: Context) {

    private var appPrefs = ctx.getSharedPreferences("epos_prefs", Context.MODE_PRIVATE)

    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) = appPrefs.registerOnSharedPreferenceChangeListener(listener)
    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) = appPrefs.unregisterOnSharedPreferenceChangeListener(listener)

    fun saveToken(token: String) {
        appPrefs.edit().putString("authToken", token).apply()
    }

    fun askToken() = appPrefs.getString("authToken", "")

    fun saveProfileId(profileId: String) {
        appPrefs.edit().putString("profileId", profileId).apply()
    }

    fun askProfileId() = appPrefs.getString("profileId", "")

    fun saveAid(aid: String) {
        appPrefs.edit().putString("aid", aid).apply()
    }

    fun askAid() = appPrefs.getString("aid", "")

}