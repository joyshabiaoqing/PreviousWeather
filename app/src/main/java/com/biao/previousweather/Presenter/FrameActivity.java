package com.biao.previousweather.Presenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;

import com.biao.previousweather.FrameApplication;
import com.biao.previousweather.R;

/**
 * Created by Administrator on 2016/7/6.
 */
public class FrameActivity extends FragmentActivity {
    private FrameApplication frameApplication;

    public FrameActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        this.frameApplication = (FrameApplication)this.getApplication();
        this.frameApplication.setCurrentActivity(this);
    }

    public void finish() {
        this.frameApplication.removeActivity(this);
        super.finish();
    }

    public FrameApplication getFrameApplication() {
        return this.frameApplication;
    }

    public void startActivity(Class<?> cls) {
        this.startActivity(new Intent(this, cls));
    }
}
