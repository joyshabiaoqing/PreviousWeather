package com.biao.previousweather.presenter.fragment.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biao.previousweather.R;
import com.biao.previousweather.presenter.fragment.BaseFragment;
import com.biao.previousweather.presenter.fragment.child.HelloFragment;
import com.biao.previousweather.presenter.fragment.child.MineFragment;
import com.biao.previousweather.view.NavigationBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/7/13.
 */
public class MainFragment extends BaseFragment {
    @BindView(R.id.nav_mine)
    NavigationBar mine;
    @BindView(R.id.nav_hello)
    NavigationBar hello;
    @BindView(R.id.vp_fragment)
    ViewPager viewPager;

    private List<BaseFragment> pagerList;
    private List<NavigationBar> navigationBars;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private Unbinder bind;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_main, viewGroup, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initialize() {
//        viewPager = (ViewPager) getView().findViewById(R.id.vp_fragment);
//        mine = (NavigationBar) getView().findViewById(R.id.nav_mine);
//        hello = (NavigationBar) getView().findViewById(R.id.nav_hello);
        pagerList = new ArrayList<>();
        navigationBars = new ArrayList<>();
        pagerList.add(new MineFragment());
        pagerList.add(new HelloFragment());

        fragmentPagerAdapter = new FragmentPagerAdapter(getMainActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return pagerList.get(position);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return pagerList.size();
            }

        };
        viewPager.setAdapter(fragmentPagerAdapter);

    }

    @Override
    protected void initControls() {
        mine.setOnClickListener(v -> {
            viewPager.setCurrentItem(0, false);
            mine.setIconAlpha(1.0f);
        });

        hello.setOnClickListener(v -> {
            viewPager.setCurrentItem(1, false);
            hello.setIconAlpha(1.0f);
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    //底部颜色渐变控制
                    navigationBars.get(position).setIconAlpha(1 - positionOffset);
                    navigationBars.get(position + 1).setIconAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                pagerList.get(position).reset();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void initData() {
        mine.setIconAlpha(1.0f);
        navigationBars.add(mine);
        navigationBars.add(hello);
        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
}
