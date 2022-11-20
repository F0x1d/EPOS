package com.f0x1d.epos.ui.fragment.lessons

import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.f0x1d.epos.R
import com.f0x1d.epos.adapter.LessonViewPagerAdapter
import com.f0x1d.epos.ui.fragment.base.BaseFragment
import com.f0x1d.epos.utils.setupColors
import com.f0x1d.epos.utils.toDayDate
import com.f0x1d.epos.viewmodel.base.BaseViewModel
import com.f0x1d.epos.viewmodel.lessons.LessonsPagerViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class LessonsPagerFragment : BaseFragment<LessonsPagerViewModel>() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private lateinit var adapter: LessonViewPagerAdapter

    private val calendar = Calendar.getInstance()

    override fun layoutId(): Int = R.layout.fragment_lessons_pager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.lessons)

        toolbar.inflateMenu(R.menu.week_menu)
        toolbar.menu.getItem(0).setOnMenuItemClickListener { viewModel.loadPrev(); true }
        toolbar.menu.getItem(1).setOnMenuItemClickListener { viewModel.loadNext(); true }

        swipeRefreshLayout = findViewById(R.id.swipe_refresh)
        swipeRefreshLayout.setupColors()
        swipeRefreshLayout.setOnRefreshListener { viewModel.reload() }

        tabLayout = findViewById(R.id.tab_layout)
        for (i in 1..7) {
            tabLayout.addTab(tabLayout.newTab())
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                updateToolbarTitle()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = LessonViewPagerAdapter(this).also { adapter = it }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                swipeRefreshLayout.isEnabled = state == ViewPager2.SCROLL_STATE_IDLE
            }
        })

        TabLayoutMediator(tabLayout, viewPager) { tab, position -> }.attach()

        viewModel.loadingStateData.observe(viewLifecycleOwner) {
            when (it) {
                BaseViewModel.LoadingState.LOADING -> {
                    swipeRefreshLayout.isRefreshing = true
                    tabLayout.visibility = View.GONE
                    viewPager.visibility = View.GONE
                }
                else -> {
                    swipeRefreshLayout.isRefreshing = false
                    viewPager.visibility = View.VISIBLE
                    tabLayout.visibility = View.VISIBLE
                }
            }
        }

        viewModel.lessonsData.observe(viewLifecycleOwner) { adapter.elements = it }

        viewModel.weekDaysData.observe(viewLifecycleOwner) {
            calendar.time = it[0]
            it.forEachIndexed { index, date ->
                tabLayout.getTabAt(index)?.text = date.toDayDate("dd")
            }
            updateToolbarTitle()
        }

        viewModel.currentDayData.observe(viewLifecycleOwner) { viewPager.setCurrentItem(it, false) }
    }

    private fun updateToolbarTitle() {
        val position = tabLayout.selectedTabPosition

        toolbar.title = calendar.run {
            set(Calendar.DAY_OF_WEEK, if (position == 6) Calendar.SUNDAY else position + 2)
            get(Calendar.DAY_OF_MONTH).toString() + " " +
                    getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) +
                    ", " +
                    getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        }
    }

    override fun viewModel(): Class<LessonsPagerViewModel> = LessonsPagerViewModel::class.java
}