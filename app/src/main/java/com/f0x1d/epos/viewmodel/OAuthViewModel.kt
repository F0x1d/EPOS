package com.f0x1d.epos.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.f0x1d.epos.EposApplication
import com.f0x1d.epos.utils.store.OkHttpClientStore
import com.f0x1d.epos.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OAuthViewModel(application: Application): BaseViewModel(application) {

    val authStateData = MutableLiveData(AuthState.LOGGING)

    fun oauthLink() = "https://school.permkrai.ru/authenticate/oauth?app=phone&mode=rsaags"

    fun grabToken(url: String) {
        authStateData.value = AuthState.GRABBING

        launchOnIO {
            EposApplication.appPreferences.saveToken(url.split("token=").last())
            OkHttpClientStore.requireClient(recreate = true)

            authStateData.postValue(AuthState.AUTHORIZED)
        }
    }

    override suspend fun processError(t: Throwable) {
        super.processError(t)
        authStateData.postValue(AuthState.LOGGING)
    }

    enum class AuthState {
        LOGGING, GRABBING, AUTHORIZED
    }
}