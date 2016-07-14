package com.biao.previousweather.presenter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biao.previousweather.presenter.MainApplication;
import com.biao.previousweather.presenter.activity.MainActivity;

import abc.FrameFragment;

public abstract class BaseFragment extends FrameFragment {
    private MainActivity mainActivity;
    private MainApplication mainApplication;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getFrameActivity();
        mainApplication = (MainApplication) getFrameApplication();
    }

    @Override
    public abstract View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle);

    @Override
    public abstract void reset();


    @Override
    public abstract boolean onBackPressed();

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public MainApplication getMainApplication() {
        return mainApplication;
    }


}
