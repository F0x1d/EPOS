package com.f0x1d.epos.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.f0x1d.epos.network.model.response.LessonResponse
import com.f0x1d.epos.ui.fragment.lessons.LessonsFragment
import com.f0x1d.epos.utils.genHash

class LessonViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    var elements = HashMap<Int, List<LessonResponse>>()
        set (value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = elements.size

    override fun createFragment(position: Int): Fragment = LessonsFragment.newInstance(elements[position + 1])

    override fun getItemId(position: Int): Long = elements[position + 1]?.firstOrNull().genHash(position.toLong())

    override fun containsItem(itemId: Long): Boolean = when (itemId) {
        5L, 6L -> true
        else -> elements.values.any {
            it.any {
                it.genHash() == itemId
            }
        }
    }
}