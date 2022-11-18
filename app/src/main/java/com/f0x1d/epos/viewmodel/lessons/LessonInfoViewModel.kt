package com.f0x1d.epos.viewmodel.lessons

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.f0x1d.epos.EposApplication
import com.f0x1d.epos.network.model.response.LessonResponse
import com.f0x1d.epos.viewmodel.base.BaseViewModel

class LessonInfoViewModel(application: Application, val lesson: LessonResponse) : BaseViewModel(application) {

    class LessonInfoViewModelFactory(private val lesson: LessonResponse): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = LessonInfoViewModel(EposApplication.instance, lesson) as T
    }
}