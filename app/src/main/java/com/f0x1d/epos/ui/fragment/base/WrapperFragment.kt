package com.f0x1d.epos.ui.fragment.base

import android.os.Bundle
import android.view.View
import com.f0x1d.epos.R
import com.f0x1d.epos.utils.fragment.FragmentNavigator
import com.f0x1d.epos.utils.fragment.MyFragmentBuilder
import com.f0x1d.epos.viewmodel.base.BaseViewModel

class WrapperFragment : BaseFragment<BaseViewModel>() {

    companion object {
        fun newInstance(tag: String, arguments: Bundle): WrapperFragment {
            val args = Bundle()
            args.putString("tag", tag)
            args.putBundle("args", arguments)

            val fragment = WrapperFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(tag: String): WrapperFragment {
            val args = Bundle()
            args.putString("tag", tag)

            val fragment = WrapperFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var fragmentNavigator: FragmentNavigator
    private val fragmentBuilder = MyFragmentBuilder()

    override fun layoutId(): Int = R.layout.fragment_wrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentNavigator = FragmentNavigator(childFragmentManager, R.id.child_container, fragmentBuilder)

        if (fragmentNavigator.currentFragment == null && savedInstanceState == null) {
            openFragment(
                requireArguments().getString("tag")!!,
                requireArguments().getBundle("args"),
                false,
                false
            )
        }
    }

    fun openFragment(tag: String, backStack: Boolean, anim: Boolean) {
        openFragment(tag, null, backStack, anim)
    }

    fun openFragment(tag: String, arguments: Bundle?, backStack: Boolean, anim: Boolean) {
        fragmentNavigator.switchTo(fragmentBuilder.getFragment(tag, arguments), tag, backStack, anim)
    }

    fun onBackPressed(): Boolean {
        if (childFragmentManager.backStackEntryCount > 0) {
            childFragmentManager.popBackStack()
            return false
        }
        return true
    }

    fun canGoBack(): Boolean = childFragmentManager.backStackEntryCount > 0

    fun popBackStackAll() {
        for (i in 0 until childFragmentManager.backStackEntryCount) {
            childFragmentManager.popBackStack()
        }
    }

    override fun viewModel(): Class<BaseViewModel>? = null
}