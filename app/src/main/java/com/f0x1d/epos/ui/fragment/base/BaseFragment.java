package com.f0x1d.epos.ui.fragment.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.f0x1d.epos.ui.activity.base.BaseActivity;

public abstract class BaseFragment<T extends AndroidViewModel> extends Fragment {

    private T mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Class<T> viewModelClass = viewModel();
        if (viewModelClass != null) {
            ViewModelProvider.Factory factory = buildFactory();

            if (factory == null)
                mViewModel = new ViewModelProvider(this).get(viewModelClass);
            else
                mViewModel = new ViewModelProvider(this, factory).get(viewModelClass);
        }
    }

    @LayoutRes
    protected abstract int layoutId();
    protected abstract Class<T> viewModel();

    protected ViewModelProvider.Factory buildFactory() {
        return null;
    }

    public WrapperFragment requireWrapperFragment() {
        Fragment fragment = getParentFragment();
        return (WrapperFragment) (fragment instanceof WrapperFragment ? fragment : fragment.getParentFragment());
    }

    public T getViewModel() {
        return mViewModel;
    }

    protected <G> G findViewById(@IdRes int id) {
        return (G) requireView().findViewById(id);
    }

    protected boolean isNightTheme() {
        return ((BaseActivity<?>) requireActivity()).isNightMode();
    }

    protected boolean isHorizontalOrientation() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}