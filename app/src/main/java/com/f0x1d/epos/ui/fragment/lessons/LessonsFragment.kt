package com.f0x1d.epos.ui.fragment.lessons

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.f0x1d.epos.R
import com.f0x1d.epos.adapter.LessonsAdapter
import com.f0x1d.epos.network.model.response.LessonResponse
import com.f0x1d.epos.ui.fragment.base.BaseFragment
import com.f0x1d.epos.utils.RecyclerViewDivider
import com.f0x1d.epos.viewmodel.lessons.LessonsViewModel

class LessonsFragment : BaseFragment<LessonsViewModel>() {

    companion object {
        fun newInstance(lessons: List<LessonResponse>?): LessonsFragment {
            val fragment = LessonsFragment()
            fragment.arguments = Bundle().apply {
                putParcelableArrayList("lessons", if (lessons != null) ArrayList(lessons) else ArrayList())
            }

            return fragment
        }
    }

    private lateinit var lessonsRecycler: RecyclerView

    private val adapter = LessonsAdapter {
        requireWrapperFragment().openFragment(
            "info_" + it.id,
            Bundle().apply {
                putParcelable("lesson", it)
            },
            true,
            true
        )
    }

    override fun layoutId(): Int = R.layout.fragment_lessons

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lessonsRecycler = findViewById(R.id.lessons_recycler)
        lessonsRecycler.layoutManager = LinearLayoutManager(requireContext())
        lessonsRecycler.addItemDecoration(RecyclerViewDivider(requireContext()))
        lessonsRecycler.adapter = adapter.also { it.elements = viewModel.lessons }
    }

    override fun buildFactory(): ViewModelProvider.Factory = LessonsViewModel.LessonsViewModelFactory(
        requireArguments().getParcelableArrayList("lessons")!!
    )

    override fun viewModel(): Class<LessonsViewModel> = LessonsViewModel::class.java
}