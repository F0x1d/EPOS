package com.f0x1d.epos.ui.activity.base;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseActivity<T extends AndroidViewModel> extends AppCompatActivity {

    private T mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setupForOldfags();
        super.onCreate(savedInstanceState);

        Class<T> viewModelClass = viewModel();
        if (viewModelClass != null) {
            ViewModelProvider.Factory factory = buildFactory();
            if (factory == null)
                mViewModel = new ViewModelProvider(this).get(viewModelClass);
            else
                mViewModel = new ViewModelProvider(this, factory).get(viewModelClass);
        }
    }

    protected abstract Class<T> viewModel();

    protected ViewModelProvider.Factory buildFactory() {
        return null;
    }

    protected T getViewModel() {
        return mViewModel;
    }

    public boolean isNightMode() {
        return (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    private void setupForOldfags() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
            getWindow().setNavigationBarColor(Color.BLACK);
        }
    }
}