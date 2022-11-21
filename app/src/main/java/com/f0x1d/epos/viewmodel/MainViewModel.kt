package com.f0x1d.epos.viewmodel

import android.app.Application
import android.content.SharedPreferences
import com.f0x1d.epos.EposApplication
import com.f0x1d.epos.utils.Event
import com.f0x1d.epos.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

class MainViewModel(application: Application): BaseViewModel(application), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        const val EVENT_TYPE_NOT_LOGGED_IN = "not_logged"
        const val EVENT_TYPE_OPEN_FRAGMENT = "open_fragment"
    }

    private val executor = Executors.newSingleThreadExecutor()

    init {
        launchOnIO {
            val token = EposApplication.appPreferences.askToken() ?: ""

            withContext(Dispatchers.Main) {
                eventsData.value = if (token.isEmpty())
                    Event(EVENT_TYPE_NOT_LOGGED_IN)
                else
                    Event(EVENT_TYPE_OPEN_FRAGMENT)
            }
        }

        EposApplication.appPreferences.registerListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String) {
        if (key == "authToken") updateAsync()
    }

    private fun updateAsync() {
        executor.execute { updateSync() }
    }

    private fun updateSync() {
        if (EposApplication.appPreferences.askToken()?.isEmpty() == true) {
            eventsData.postValue(Event(EVENT_TYPE_NOT_LOGGED_IN))
        }
    }

    override fun onCleared() {
        super.onCleared()
        EposApplication.appPreferences.unregisterListener(this)
    }

}