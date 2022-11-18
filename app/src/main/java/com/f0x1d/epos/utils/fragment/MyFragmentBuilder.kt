package com.f0x1d.epos.utils.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.f0x1d.epos.ui.fragment.LessonInfoFragment
import com.f0x1d.epos.ui.fragment.MarksFragment
import com.f0x1d.epos.ui.fragment.lessons.LessonsPagerFragment

class MyFragmentBuilder(): FragmentNavigator.FragmentBuilder {
    override fun getFragment(tag: String): Fragment {
        if (tag.startsWith("info_"))
            return LessonInfoFragment()

        return when (tag) {
            "marks" -> MarksFragment()
            else -> LessonsPagerFragment()
        }
    }

    fun getFragment(tag: String, arguments: Bundle?): Fragment {
        val fragment = getFragment(tag)
        if (arguments != null) fragment.arguments = arguments
        return fragment
    }
}