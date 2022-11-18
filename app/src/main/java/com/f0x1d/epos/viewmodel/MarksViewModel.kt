package com.f0x1d.epos.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.f0x1d.epos.network.model.response.AllMarksResponse
import com.f0x1d.epos.network.repository.MarksRepository
import com.f0x1d.epos.viewmodel.base.BaseViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MarksViewModel(application: Application): BaseViewModel(application) {

    val allMarksData = MutableLiveData<List<AllMarksResponse>>()

    private val marksRepository = MarksRepository()

    init {
        load()
    }

    fun load() {
        loadingStateData.value = LoadingState.LOADING

        launchOnMain {
            allMarksData.value = marksRepository.requestData()
            loadingStateData.value = LoadingState.LOADED
        }
    }
}