package com.f0x1d.epos.utils.fragment;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentNavigator {

    private final FragmentManager mFragmentManager;
    @IdRes
    private final int mContainerView;
    private final FragmentBuilder mFragmentBuilder;

    public FragmentNavigator(FragmentManager fragmentManager, int containerView, FragmentBuilder fragmentBuilder) {
        this.mFragmentManager = fragmentManager;
        this.mContainerView = containerView;
        this.mFragmentBuilder = fragmentBuilder;
    }

    public void switchTo(String tag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);

        Fragment currentFragment = getCurrentFragment();
        if (currentFragment != null)
            fragmentTransaction.hide(currentFragment);

        Fragment targetFragment = mFragmentManager.findFragmentByTag(tag);
        if (targetFragment == null)
            fragmentTransaction.add(mContainerView, mFragmentBuilder.getFragment(tag), tag);
        else
            fragmentTransaction.show(targetFragment);

        fragmentTransaction.commitNow();
    }

    public void switchTo(Fragment fragment, String tag, boolean backStack, boolean anim) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (anim)
            fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);

        Fragment currentFragment = getCurrentFragment();
        if (currentFragment != null)
            fragmentTransaction.hide(currentFragment);

        Fragment targetFragment = mFragmentManager.findFragmentByTag(tag);
        if (targetFragment == null)
            fragmentTransaction.add(mContainerView, fragment, tag);
        else
            fragmentTransaction.show(targetFragment);

        if (backStack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void popBackStack() {
        mFragmentManager.popBackStack();
    }

    @Nullable
    public Fragment getCurrentFragment() {
        for (Fragment fragment : mFragmentManager.getFragments()) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    public interface FragmentBuilder {
        Fragment getFragment(String tag);
    }
}