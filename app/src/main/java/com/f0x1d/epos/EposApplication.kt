package com.f0x1d.epos

import android.app.Application
import com.f0x1d.epos.utils.AppPreferences
import com.google.android.material.color.DynamicColors

class EposApplication: Application() {

    companion object {
        lateinit var instance: EposApplication
        lateinit var appPreferences: AppPreferences
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        DynamicColors.applyToActivitiesIfAvailable(this)

        appPreferences = AppPreferences(this)
    }
}