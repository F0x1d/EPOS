package com.f0x1d.epos.viewmodel.lessons

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.f0x1d.epos.network.model.response.*
import com.f0x1d.epos.network.repository.LessonsRepository
import com.f0x1d.epos.utils.store.OkHttpClientStore
import com.f0x1d.epos.utils.weekDays
import com.f0x1d.epos.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class LessonsPagerViewModel(application: Application) : BaseViewModel(application) {

    val weekDaysData = MutableLiveData<List<Date>>()
    val lessonsData = MutableLiveData<HashMap<Int, List<LessonResponse>>>()
    val currentDayData = MutableLiveData<Int>()

    private val lessonsRepository = LessonsRepository()

    private var currentOffset = 0

    init {
        load()
    }

    fun loadNext() {
        load(currentOffset + 1)
    }

    fun loadPrev() {
        load(currentOffset - 1)
    }

    fun reload() {
        load(currentOffset)
    }

    private fun load(offset: Int = 0) {
        if (loadingStateData.value == LoadingState.LOADING) return

        loadingStateData.value = LoadingState.LOADING
        currentOffset = offset

        launchOnMain {
            lessonsData.value = lessonsRepository
                .updateValues(arrayOf(offset))
                .requestData()

            weekDaysData.value = weekDays(offset)

            loadingStateData.value = LoadingState.LOADED

            if (currentDayData.value == null) {
                var currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2
                if (currentDayOfWeek == -1)
                    currentDayOfWeek = 6

                currentDayData.value = currentDayOfWeek
            }
        }
    }
}