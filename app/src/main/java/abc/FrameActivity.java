package abc;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.AnimRes;
import android.support.v4.app.FragmentActivity;

import com.biao.previousweather.R;

/**
 * Created by Administrator on 2016/7/6.
 */
public class FrameActivity extends FragmentActivity {
    private FrameApplication frameApplication;
    private Class<? extends FrameFragment> currentFragmentClass;
    private int containerViewId;

    public FrameActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        this.frameApplication = (FrameApplication) this.getApplication();
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

    public FrameFragment findFragmentByTag(String fragmentName) {
        return (FrameFragment) (this.getSupportFragmentManager().findFragmentByTag(fragmentName));
    }

    public void addAndShowFragment(int container, FrameFragment addFragment, @AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        this.getSupportFragmentManager().beginTransaction().setCustomAnimations(enter, exit, popEnter, popExit).add(container, addFragment, addFragment.getClass().getName()).commit();
    }

    public void hideAndAdd(int container, String hideFragment, FrameFragment addFragment, @AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        this.getSupportFragmentManager().beginTransaction().setCustomAnimations(enter, exit, popEnter, popExit).hide(this.findFragmentByTag(hideFragment)).add(container, addFragment, addFragment.getClass().getName()).commit();
    }

    public void hideAndShowFragment(String hideFragment, String showFragment, @AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        this.getSupportFragmentManager().beginTransaction().setCustomAnimations(enter, exit, popEnter, popExit).hide(this.findFragmentByTag(hideFragment)).show(this.findFragmentByTag(showFragment)).commit();
    }

    public void changeFragment(Class<? extends FrameFragment> fragmentClass) {
        this.changeFragment(fragmentClass, R.anim.push_top_in, R.anim.push_top_out, R.anim.push_right_in, R.anim.push_right_out);
    }

    public void changeFragment(Class<? extends FrameFragment> fragmentClass, @AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        Class fragment = this.changeFragment(this.containerViewId, this.currentFragmentClass, fragmentClass, enter, exit, popEnter, popExit);
        if (fragment != null) {
            this.currentFragmentClass = fragment;
        }
    }


    public FrameFragment getCurrentFragment() {
        return this.findFragmentByTag(this.currentFragmentClass.getName());
    }

    public FrameFragment getCurrentChildFragment() {
        return this.getCurrentFragmentClass() != null ? this.findFragmentByTag(this.getCurrentFragment().getCurrentChildClass().getName()) : null;
    }

    public Class<? extends FrameFragment> getCurrentFragmentClass() {
        return this.currentFragmentClass;
    }

    public Class<? extends FrameFragment> changeFragment(int container, Class<? extends FrameFragment> fragment, Class<? extends FrameFragment> fragmentClass, @AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        if (fragment != fragmentClass) {
            FrameFragment frameFragment;
            if ((frameFragment = this.findFragmentByTag(fragmentClass.getName())) == null) {
                try {
                    frameFragment = (FrameFragment) fragmentClass.newInstance();
                } catch (InstantiationException var10) {
                    var10.printStackTrace();
                } catch (IllegalAccessException var11) {
                    var11.printStackTrace();
                }

                if (fragment == null) {
                    this.addAndShowFragment(container, frameFragment, enter, exit, popEnter, popExit);
                } else {
                    this.hideAndAdd(container, fragment.getName(), frameFragment, enter, exit, popEnter, popExit);
                }
            } else if (frameFragment.isHidden()) {
                this.hideAndShowFragment(fragment.getName(), fragmentClass.getName(), enter, exit, popEnter, popExit);
            }

            if (frameFragment != null && frameFragment.getLastFragment() == null) {
                frameFragment.setLastFragment(fragment);
            }

            return fragmentClass;
        } else {
            return null;
        }
    }

    public int getContainerViewId() {
        return this.containerViewId;
    }

    public void setContainerViewId(int containerViewId) {
        this.containerViewId = containerViewId;
    }
}
