package com.biao.previousweather.presenter.activity;

import android.os.Bundle;

import com.biao.previousweather.presenter.fragment.main.MainFragment;
import com.biao.previousweather.R;

import abc.FrameActivity;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends FrameActivity {

    protected CompositeSubscription compositeSubscription = new CompositeSubscription();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setContainerViewId(R.id.contentView);

//        changeFragment();
        changeFragment(MainFragment.class);
    }

    @Override
    public void onBackPressed() {
    }
}
