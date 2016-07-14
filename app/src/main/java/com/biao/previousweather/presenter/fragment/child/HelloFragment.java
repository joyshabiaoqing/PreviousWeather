package com.biao.previousweather.presenter.fragment.child;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biao.previousweather.presenter.fragment.BaseFragment;
import com.biao.previousweather.R;

/**
 * Created by Administrator on 2016/7/13.
 */
public class HelloFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_hello, viewGroup, false);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void initControls() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public void reset() {

    }
}
