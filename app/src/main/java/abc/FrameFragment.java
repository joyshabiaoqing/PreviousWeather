package abc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Administrator on 2016/7/5.
 */
public abstract class FrameFragment extends Fragment {
    public static final int REFRESH_FRAGMENT = 0;
    private FrameActivity frameActivity;
    private Class<? extends FrameFragment> lastFragment;
    private Class<? extends FrameFragment> currentChildClass;
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

//    public void onSaveInstanceState(Bundle outState) {
//        FrameSaveRestoreHolder.saveInstance(outState, this);
//        super.onSaveInstanceState(outState);
//    }

//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        if(savedInstanceState != null) {
//            this.isDestroy = true;
//            FrameSaveRestoreHolder.restoreInstance(savedInstanceState, this);
//        }
//
//        super.onViewStateRestored(savedInstanceState);
//    }

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


    public FrameActivity getFrameActivity() {
        return this.frameActivity;
    }

    public void setFrameActivity(FrameActivity activity) {
        this.frameActivity = activity;
    }

    public FrameApplication getFrameApplication() {
        return (FrameApplication)this.getActivity().getApplication();
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


}
