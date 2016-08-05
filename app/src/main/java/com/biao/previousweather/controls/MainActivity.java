package com.biao.previousweather.controls;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.biao.previousweather.R;
import com.biao.previousweather.controls.adapter.WeatherAdapter;
import com.biao.previousweather.controls.component.RetrofitSingleton;
import com.biao.previousweather.controls.listener.HidingScrollListener;
import com.biao.previousweather.controls.utils.PLog;
import com.biao.previousweather.controls.utils.Util;
import com.biao.previousweather.modules.Weather;

import rx.Observable;
import rx.Observer;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/8/2.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private SwipeRefreshLayout mRefreshLayout;
    private ImageView bannner;
    private ProgressBar mProgressBar;
    private ImageView mErroImageView;
    private RelativeLayout headerBackground;
    private RecyclerView mRecyclerView;

    private Observer<Weather> observer;
    private Weather mWeather = new Weather();
    private WeatherAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity2);
    }

    @Override
    protected void initialize() {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        if (appBarLayout != null) {
            //控制是否展开
            appBarLayout.setExpanded(false);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bannner = (ImageView) findViewById(R.id.banner);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mErroImageView = (ImageView) findViewById(R.id.iv_erro);
        // Glide 加载本地 GIF 图的方法
        //GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(mErroImageView);
        //Glide.with(this).load(R.raw.loading).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewTarget);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        if (mRefreshLayout != null) {
            mRefreshLayout.setOnRefreshListener(() ->
                    mRefreshLayout.postDelayed(this::load, 1000));
        }

        //标题
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle(" ");
        }

        initIcon();

        //初始化抽屉
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
            headerBackground = (RelativeLayout) headerLayout.findViewById(R.id.header_background);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }


        //fab
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
//            fab.setOnClickListener(v -> showFabDialog());
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            final int fabBottomMargin = lp.bottomMargin;

            //recclerview
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.addOnScrollListener(new HidingScrollListener() {
                @Override
                public void onHide() {
                    fab.animate()
                            .translationY(fab.getHeight() + fabBottomMargin)
                            .setInterpolator(new AccelerateInterpolator(2))
                            .start();
                }

                @Override
                public void onShow() {
                    fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                }
            });
            mAdapter = new WeatherAdapter(MainActivity.this, mWeather);
            mRecyclerView.setAdapter(mAdapter);

//            mAdapter.setOnItemClickListener(mWeather1 -> {
//                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View dialogLayout = inflater.inflate(R.layout.weather_dialog, (ViewGroup) this.findViewById(
//                        R.id.weather_dialog_root));
//                AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                        .setView(dialogLayout);
//                final AlertDialog alertDialog = builder.create();
//
//                RelativeLayout root = (RelativeLayout) dialogLayout.findViewById(R.id.weather_dialog_root);
//                switch (Util.getWeatherType(Integer.parseInt(mWeather1.now.cond.code))) {
//                    case "晴":
//                        root.setBackgroundResource(R.mipmap.dialog_bg_sunny);
//                        break;
//                    case "阴":
//                        root.setBackgroundResource(R.mipmap.dialog_bg_cloudy);
//                        break;
//                    case "雨":
//                        root.setBackgroundResource(R.mipmap.dialog_bg_rainy);
//                        break;
//                    default:
//                        break;
//                }
//
//                TextView city = (TextView) dialogLayout.findViewById(R.id.dialog_city);
//                city.setText(mWeather1.basic.city);
//                TextView temp = (TextView) dialogLayout.findViewById(R.id.dialog_temp);
//                temp.setText(String.format("%s°", mWeather1.now.tmp));
//                ImageView icon = (ImageView) dialogLayout.findViewById(R.id.dialog_icon);
//
//                Glide.with(this)
//                        .load(mSetting.getInt(mWeather1.now.cond.txt, R.mipmap.none))
//                        .asBitmap()
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                icon.setImageBitmap(resource);
//                                icon.setColorFilter(Color.WHITE);
//                            }
//                        });
//
//                alertDialog.show();
//            });
        }
    }

    @Override
    protected void initControls() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    /**
     * 优化网络+缓存逻辑
     * 优先网络
     */
    private void load() {

        compositeSubscription.add(Observable.concat(fetchDataByNetWork(), fetchDataByCache())
                .first(new Func1<Weather, Boolean>() {
                    @Override
                    public Boolean call(Weather weather) {
                        return weather != null;
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                })
                .doOnNext(new Action1<Weather>() {
                    @Override
                    public void call(Weather weather) {

                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(observer)
        );

//        compositeSubscription.add(
//                Observable.concat(fetchDataByNetWork(), fetchDataByCache())
//                        .first(weather -> weather != null)
//                        .doOnError(throwable -> {
//                            mErroImageView.setVisibility(View.VISIBLE);
//                            mRecyclerView.setVisibility(View.GONE);
//                        })
//                        .doOnNext(weather -> {
//                            mErroImageView.setVisibility(View.GONE);
//                            mRecyclerView.setVisibility(View.VISIBLE);
//                        })
//                        .doOnTerminate(() -> {
//                            mRefreshLayout.setRefreshing(false);
//                            mProgressBar.setVisibility(View.GONE);
//                        })
//                        .subscribe(observer)
//        );
    }

    /**
     * 从网络获取
     */
    private Observable<Weather> fetchDataByNetWork() {
        String cityName = Util.replaceCity(mSetting.getCityName());
        return RetrofitSingleton.getInstance()
                .fetchWeather(cityName)
                .onErrorReturn(throwable -> {
                    PLog.e(throwable.getMessage());
//                    ToastUtil.showLong(throwable.getMessage());
                    return null;
                });
    }

    /**
     * 从本地获取
     */
    private Observable<Weather> fetchDataByCache() {
        return Observable.defer(() -> {
//            Weather weather = (Weather) aCache.getAsObject(C.WEATHER_CACHE);
            Weather weather = new Weather();
            return Observable.just(weather);
        });
    }

    /**
     * 初始化 observer (观察者)
     * 拿到数据后的操作
     */
    private void initDataObserver() {

        observer = new Observer<Weather>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                PLog.e(e.toString());
                Snackbar.make(fab, "网络不好,~( ´•︵•` )~", Snackbar.LENGTH_INDEFINITE).setAction("重试", v -> {
                    load();
                }).show();
            }

            @Override
            public void onNext(Weather weather) {
//                if (mSetting.getAutoUpdate() == 0) {
//                    aCache.put(C.WEATHER_CACHE, weather);
//                } else {
//                    aCache.put(C.WEATHER_CACHE, weather,
//                            (mSetting.getAutoUpdate() * Setting.ONE_HOUR));//默认3小时后缓存失效
//                }
                mWeather.status = weather.status;
                mWeather.aqi = weather.aqi;
                mWeather.basic = weather.basic;
                mWeather.suggestion = weather.suggestion;
                mWeather.now = weather.now;
                mWeather.dailyForecast = weather.dailyForecast;
                mWeather.hourlyForecast = weather.hourlyForecast;
                collapsingToolbarLayout.setTitle(mWeather.basic.city);
                //mAdapter = new WeatherAdapter(MainActivity.this, weather);
                //mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                normalStyleNotification(mWeather);
                showSnackbar(fab, "加载完毕，✺◟(∗❛ัᴗ❛ั∗)◞✺,");
            }
        };
    }

    private void normalStyleNotification(Weather weather) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(MainActivity.this);
        Notification notification = builder.setContentIntent(pendingIntent)
                .setContentTitle(weather.basic.city)
                .setContentText(String.format("%s 当前温度: %s℃ ", weather.now.cond.txt, weather.now.tmp))
                // 这里部分 ROM 无法成功
                .setSmallIcon(mSetting.getInt(weather.now.cond.txt, R.mipmap.none))
                .build();
        notification.flags = mSetting.getNotificationModel();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // tag和id都是可以拿来区分不同的通知的
        manager.notify(1, notification);
    }

    /**
     * 初始化Icon
     */
    private void initIcon() {
        if (mSetting.getIconType() == 0) {
            mSetting.putInt("未知", R.mipmap.none);
            mSetting.putInt("晴", R.mipmap.type_one_sunny);
            mSetting.putInt("阴", R.mipmap.type_one_cloudy);
            mSetting.putInt("多云", R.mipmap.type_one_cloudy);
            mSetting.putInt("少云", R.mipmap.type_one_cloudy);
            mSetting.putInt("晴间多云", R.mipmap.type_one_cloudytosunny);
            mSetting.putInt("小雨", R.mipmap.type_one_light_rain);
            mSetting.putInt("中雨", R.mipmap.type_one_light_rain);
            mSetting.putInt("大雨", R.mipmap.type_one_heavy_rain);
            mSetting.putInt("阵雨", R.mipmap.type_one_thunderstorm);
            mSetting.putInt("雷阵雨", R.mipmap.type_one_thunder_rain);
            mSetting.putInt("霾", R.mipmap.type_one_fog);
            mSetting.putInt("雾", R.mipmap.type_one_fog);
        } else {
            mSetting.putInt("未知", R.mipmap.none);
            mSetting.putInt("晴", R.mipmap.type_two_sunny);
            mSetting.putInt("阴", R.mipmap.type_two_cloudy);
            mSetting.putInt("多云", R.mipmap.type_two_cloudy);
            mSetting.putInt("少云", R.mipmap.type_two_cloudy);
            mSetting.putInt("晴间多云", R.mipmap.type_two_cloudytosunny);
            mSetting.putInt("小雨", R.mipmap.type_two_light_rain);
            mSetting.putInt("中雨", R.mipmap.type_two_rain);
            mSetting.putInt("大雨", R.mipmap.type_two_rain);
            mSetting.putInt("阵雨", R.mipmap.type_two_rain);
            mSetting.putInt("雷阵雨", R.mipmap.type_two_thunderstorm);
            mSetting.putInt("霾", R.mipmap.type_two_haze);
            mSetting.putInt("雾", R.mipmap.type_two_fog);
            mSetting.putInt("雨夹雪", R.mipmap.type_two_snowrain);
        }
    }

}
