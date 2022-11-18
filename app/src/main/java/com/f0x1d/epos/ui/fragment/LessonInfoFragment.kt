package com.f0x1d.epos.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.f0x1d.epos.R
import com.f0x1d.epos.adapter.lessoninfo.HomeworkAdapter
import com.f0x1d.epos.adapter.lessoninfo.LessonMarksAdapter
import com.f0x1d.epos.adapter.lessoninfo.TimeAdapter
import com.f0x1d.epos.ui.fragment.base.BaseFragment
import com.f0x1d.epos.viewmodel.lessons.LessonInfoViewModel
import com.google.android.material.appbar.MaterialToolbar

class LessonInfoFragment: BaseFragment<LessonInfoViewModel>() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var lessonInfoRecycler: RecyclerView

    private val timeAdapter = TimeAdapter()
    private val marksAdapter = LessonMarksAdapter()
    private val homeworkAdapter = HomeworkAdapter()

    override fun layoutId(): Int = R.layout.fragment_lesson_info

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = viewModel.lesson.subjectName
        toolbar.setNavigationOnClickListener { requireWrapperFragment().onBackPressed() }

        lessonInfoRecycler = findViewById(R.id.lesson_info_recycler)
        lessonInfoRecycler.layoutManager = LinearLayoutManager(requireContext())
        lessonInfoRecycler.adapter = ConcatAdapter(
            timeAdapter.apply { elements = listOf(viewModel.lesson) },
            marksAdapter.apply { elements = if (viewModel.lesson.showMarks()) listOf(viewModel.lesson) else emptyList() },
            homeworkAdapter.apply { elements = viewModel.lesson.homeworks }
        )
    }

    override fun buildFactory(): ViewModelProvider.Factory = LessonInfoViewModel.LessonInfoViewModelFactory(
        requireArguments().getParcelable("lesson")!!
    )

    override fun viewModel(): Class<LessonInfoViewModel> = LessonInfoViewModel::class.java
}