package com.biao.previousweather.Presenter;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import info.mixun.baseframework.annotations.FrameSaveRestoreHolder;
import info.mixun.baseframework.control.FrameApplication;
import info.mixun.baseframework.control.activity.FrameActivity;
import info.mixun.baseframework.control.handler.FrameHandler;

/**
 * Created by Administrator on 2016/7/5.
 */
public abstract class FrameFragment extends Fragment {
    public static final int REFRESH_FRAGMENT = 0;
    private FrameActivity frameActivity = null;
    private FrameHandler<? extends FrameFragment> handler;
    private Class<? extends FrameFragment> lastFragment = null;
    private Class<? extends FrameFragment> currentChildClass = null;
    private int sonViewId;
    private boolean isDestroy = true;

    public FrameFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.frameActivity = (FrameActivity)this.getActivity();
    }

    public abstract View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3);


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
        this.initControls();
    }

    public void onHiddenChanged(boolean hidden) {
        if(hidden) {
            this.hide();
        } else {
            this.reset();
        }

        if(this.currentChildClass != null && this.getFrameActivity().findFragmentByTag(this.currentChildClass.getName()) != null) {
            this.getFrameActivity().findFragmentByTag(this.currentChildClass.getName()).onHiddenChanged(hidden);
        }

        super.onHiddenChanged(hidden);
    }

    public void onSaveInstanceState(Bundle outState) {
        FrameSaveRestoreHolder.saveInstance(outState, this);
        super.onSaveInstanceState(outState);
    }

    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            this.isDestroy = true;
            FrameSaveRestoreHolder.restoreInstance(savedInstanceState, this);
        }

        super.onViewStateRestored(savedInstanceState);
    }

    public void onResume() {
        if(this.isDestroy) {
            this.initData();
            this.isDestroy = false;
        }

        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    protected abstract void initialize();

    protected abstract void initControls();

    protected void initData() {
    }

    protected void hide() {
    }



    public abstract void reset();

    public abstract boolean onBackPressed();

    public void refresh(int i) {
        if(this.getHandler() != null) {
            this.handler.sendMessage(this.getHandler().obtainMessage(i));
        }

    }

    public void refresh(int i, Serializable data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        this.refresh(i, bundle);
    }

    public void refresh(int i, Bundle bundle) {
        if(this.getHandler() != null) {
            Message msg = this.getHandler().obtainMessage(i);
            msg.setData(bundle);
            this.handler.sendMessage(msg);
        }

    }


    public FrameActivity getFrameActivity() {
        return this.frameActivity;
    }

    public void setFrameActivity(FrameActivity activity) {
        this.frameActivity = activity;
    }

    public FrameApplication getFrameApplication() {
        return (FrameApplication)this.getActivity().getApplication();
    }

    public FrameHandler<? extends FrameFragment> getHandler() {
        return this.handler;
    }

    public void setHandler(FrameHandler<? extends FrameFragment> handler) {
        this.handler = handler;
    }

    public Class<? extends FrameFragment> getLastFragment() {
        return this.lastFragment;
    }

    public void setLastFragment(Class<? extends FrameFragment> lastFragment) {
        this.lastFragment = lastFragment;
    }

    public Class<? extends FrameFragment> getCurrentChildClass() {
        return this.currentChildClass;
    }


    public void setSonViewId(int sonViewId) {
        this.sonViewId = sonViewId;
    }
}
