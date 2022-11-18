package com.f0x1d.epos.viewmodel.base

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.f0x1d.epos.EposApplication
import com.f0x1d.epos.network.UnauthorizedException
import com.f0x1d.epos.utils.Event
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application): AndroidViewModel(application) {

    val loadingStateData = MutableLiveData<LoadingState>()
    val eventsData = MutableLiveData<Event>()

    open suspend fun processError(t: Throwable) {
        if (t is CancellationException) {
            return
        }

        if (t is UnauthorizedException) {
            EposApplication.appPreferences.saveToken("")
            withContext(Dispatchers.Main) {
                Toast.makeText(getApplication(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            return
        }

        t.printStackTrace()
        withContext(Dispatchers.Main) {
            Toast.makeText(getApplication(), t.localizedMessage, Toast.LENGTH_LONG).show()
        }

        loadingStateData.postValue(LoadingState.ERROR)
    }

    protected fun launchOnIO(block: suspend CoroutineScope.() -> Unit) = launchOn(Dispatchers.IO, block)
    protected fun launchOnMain(block: suspend CoroutineScope.() -> Unit) = launchOn(Dispatchers.Main, block)

    protected fun launchOn(where: CoroutineContext, block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch(where) {
        try {
            coroutineScope {
                block.invoke(this)
            }
        } catch (e: Exception) {
            processError(e)
        }
    }

    enum class LoadingState {
        LOADING, LOADED, ERROR
    }
}