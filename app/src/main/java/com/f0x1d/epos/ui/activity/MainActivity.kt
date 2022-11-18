package com.f0x1d.epos.ui.activity

import android.content.Intent
import android.os.Bundle
import com.f0x1d.epos.R
import com.f0x1d.epos.ui.activity.base.BaseActivity
import com.f0x1d.epos.ui.fragment.base.WrapperFragment
import com.f0x1d.epos.utils.fragment.FragmentNavigator
import com.f0x1d.epos.utils.fragment.MyFragmentBuilder
import com.f0x1d.epos.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.MaterialShapeDrawable

class MainActivity : BaseActivity<MainViewModel>() {

    private lateinit var bottomNavigation: BottomNavigationView

    private lateinit var fragmentNavigator: FragmentNavigator
    private val bottomTabs = HashMap<Int, String>().apply {
        put(R.id.lessons_item, "lessons")
        put(R.id.marks_item, "marks")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentNavigator = FragmentNavigator(supportFragmentManager, R.id.main_container, MyFragmentBuilder())

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener {
            val tag = bottomTabs[it.itemId] ?: ""
            fragmentNavigator.switchTo(WrapperFragment.newInstance(tag), tag, false, true)

            return@setOnItemSelectedListener true
        }
        window.navigationBarColor = (bottomNavigation.background as MaterialShapeDrawable).resolvedTintColor

        viewModel.eventsData.observe(this) { event ->
            if (event.isConsumed) return@observe

            when (event.type()) {
                MainViewModel.EVENT_TYPE_NOT_LOGGED_IN -> {
                    startActivity(Intent(this, OAuthActivity::class.java))
                    finish()
                }
                MainViewModel.EVENT_TYPE_OPEN_FRAGMENT -> bottomNavigation.selectedItemId = R.id.lessons_item
            }
        }
    }

    override fun onBackPressed() {
        val currentFragment = fragmentNavigator.currentFragment
        if (currentFragment != null) {
            if (!(currentFragment as WrapperFragment).onBackPressed()) return
        }
        super.onBackPressed()
    }

    override fun viewModel(): Class<MainViewModel> = MainViewModel::class.java
}