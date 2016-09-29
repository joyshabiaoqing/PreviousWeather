package com.biao.previousweather.controls.component;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.biao.previousweather.controls.utils.C;
import com.biao.previousweather.controls.utils.RxUtils;
import com.biao.previousweather.modules.VersionAPI;
import com.biao.previousweather.modules.Weather;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class RetrofitSingleton {

    private static APIService apiService = null;
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;
    private static final String TAG = RetrofitSingleton.class.getSimpleName();

    public static void init() {
        initOkHttp();
        initRetrofit();
        apiService = retrofit.create(APIService.class);
    }

    public static RetrofitSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final RetrofitSingleton INSTANCE = new RetrofitSingleton();
    }

    private static void initOkHttp() {
        // https://drakeet.me/retrofit-2-0-okhttp-3-0-config
        //HttpLoggingInterceptor 是一个拦截器，用于输出网络请求和结果的 Log，可以配置 level 为 BASIC / HEADERS / BODY，都很好理解，
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
//                .addNetworkInterceptor(mTokenInterceptor)
//                .authenticator(mAuthenticator)
                .build();
    }

//    addNetworkInterceptor 让所有网络请求都附上你的拦截器，我这里设置了一个 token 拦截器，就是在所有网络请求的 header 加上 token 参数，下面会稍微讲一下这个内容。
//    让所有网络请求都附上你的拦截器：
//
//    Interceptor mTokenInterceptor = new Interceptor() {
//        @Override public Response intercept(Chain chain) throws IOException {
//            Request originalRequest = chain.request();
//            if (Your.sToken == null || alreadyHasAuthorizationHeader(originalRequest)) {
//                return chain.proceed(originalRequest);
//            }
//            Request authorised = originalRequest.newBuilder()
//                    .header("Authorization", Your.sToken)
//                    .build();
//            return chain.proceed(authorised);
//        }
//    };

    //如果你需要在遇到诸如 401 Not Authorised 的时候进行刷新 token，可以使用 Authenticator，这是一个专门设计用于当验证出现错误的时候，进行询问获取处理的拦截器：
//    Authenticator mAuthenticator = new Authenticator() {
//        @Override public Request authenticate(Route route, Response response)
//                throws IOException {
//            Your.sToken = service.refreshToken();
//            return response.request().newBuilder()
//                    .addHeader("Authorization", newAccessToken)
//                    .build();
//        }
//    }


    private static void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(APIService.HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static void disposeFailureInfo(Throwable t, Context context, View view) {
        if (t.toString().contains("GaiException") || t.toString().contains("SocketTimeoutException") ||
                t.toString().contains("UnknownHostException")) {
            Snackbar.make(view, "网络不好,~( ´•︵•` )~", Snackbar.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
        }
//        PLog.w(t.toString());
    }

    public Observable<Weather> fetchWeather(String city) {
//        return apiService.mWeatherAPI(city, C.KEY)
//                //.filter(weatherAPI -> weatherAPI.mHeWeatherDataService30s.get(0).status.equals("ok"))
//                .flatMap(weatherAPI -> {
//                    if (weatherAPI.mHeWeatherDataService30s.get(0).status.equals("no more requests")) {
//                        return Observable.error(new RuntimeException("/(ㄒoㄒ)/~~,API免费次数已用完"));
//                    }
//                    return Observable.just(weatherAPI);
//                })
//                .map(weatherAPI -> weatherAPI.mHeWeatherDataService30s.get(0))
//                .compose(RxUtils.rxSchedulerHelper());
        return apiService.mWeatherAPI(city, C.KEY)
                .flatMap(weatherAPI -> {
                    if (weatherAPI.mHeWeatherDataService30s.get(0).status.equals("no more requests")) {
                        return Observable.error(new RuntimeException("api免费次数已经用完"));
                    } else {
                        return Observable.just(weatherAPI);
                    }
                })
                .map(weatherAPI -> weatherAPI.mHeWeatherDataService30s.get(0))
                .compose(RxUtils.rxSchedulerHelper());
    }

    public Observable<VersionAPI> fetchVersion() {
        return apiService.mVersionAPI(C.API_TOKEN).compose(RxUtils.rxSchedulerHelper());
    }
}
