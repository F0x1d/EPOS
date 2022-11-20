package com.f0x1d.epos.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.f0x1d.epos.R
import com.f0x1d.epos.adapter.marks.AllMarksAdapter
import com.f0x1d.epos.ui.dialog.InfoBottomSheet
import com.f0x1d.epos.ui.fragment.base.BaseFragment
import com.f0x1d.epos.utils.RecyclerViewDivider
import com.f0x1d.epos.utils.mapWeight
import com.f0x1d.epos.utils.setupColors
import com.f0x1d.epos.viewmodel.MarksViewModel
import com.f0x1d.epos.viewmodel.base.BaseViewModel
import com.google.android.material.appbar.MaterialToolbar

class MarksFragment: BaseFragment<MarksViewModel>() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var marksRecycler: RecyclerView

    private val adapter = AllMarksAdapter { mark ->
        InfoBottomSheet.newInstance(
            mark.values.firstOrNull()?.original.toString() + mark.weight.mapWeight(),
            mark.date + ": " + mark.controlFormName
        ).show(childFragmentManager, "ModalBottomSheet")
    }

    override fun layoutId(): Int = R.layout.fragment_marks

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.marks)

        swipeRefreshLayout = findViewById(R.id.swipe_refresh)
        swipeRefreshLayout.setupColors()
        swipeRefreshLayout.setOnRefreshListener { viewModel.load() }

        marksRecycler = findViewById(R.id.all_marks_recycler)
        marksRecycler.layoutManager = LinearLayoutManager(requireContext())
        marksRecycler.addItemDecoration(RecyclerViewDivider(requireContext()))
        marksRecycler.adapter = adapter

        viewModel.loadingStateData.observe(viewLifecycleOwner) {
            when (it) {
                BaseViewModel.LoadingState.LOADING -> {
                    swipeRefreshLayout.isRefreshing = true
                    marksRecycler.visibility = View.GONE
                }
                else -> {
                    swipeRefreshLayout.isRefreshing = false
                    marksRecycler.visibility = View.VISIBLE
                }
            }
        }

        viewModel.allMarksData.observe(viewLifecycleOwner) { adapter.elements = it }
    }

    override fun viewModel(): Class<MarksViewModel> = MarksViewModel::class.java
}