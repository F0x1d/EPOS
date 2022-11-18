package com.f0x1d.epos.viewmodel.lessons

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.f0x1d.epos.EposApplication
import com.f0x1d.epos.network.model.response.LessonResponse
import com.f0x1d.epos.viewmodel.base.BaseViewModel

class LessonsViewModel(application: Application, val lessons: List<LessonResponse>): BaseViewModel(application) {

    class LessonsViewModelFactory(private val lessons: List<LessonResponse>): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = LessonsViewModel(EposApplication.instance, lessons) as T
    }
}