package com.biao.previousweather.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import abc.FrameApplication;

/**
 * Created by Administrator on 2015/10/16.
 */
public class MainApplication extends FrameApplication {


    @Override
    public void onCreate() {
        super.onCreate();

//        异常处理
//        FrameCrashHandler.init(this, getString(R.string.exception_message), new FrameExceptionListener() {
//            @Override
//            public void doWithException(Map<String, String> info) {
//                AppExceptionData appExceptionData = new AppExceptionData();
//                appExceptionData.setVersionCode(info.get("versionCode"));
//                appExceptionData.setVersionName(info.get("versionName"));
//                appExceptionData.setPackageName(info.get("packageName"));
//                appExceptionData.setExceptionDetail(info.get("exception"));
//                appExceptionData.setPackageInfo(info.get("packageInfo"));
//                appExceptionData.setBuildData(info.get("buildData"));
//                appExceptionData.setBuildVersion(info.get("buildVersion"));
//                if (FastSocketMain.getInstance().getClientMessageWorker() != null)
//                    FastSocketMain.getInstance().getClientMessageWorker().sendDataLn(new FastSocketData<AppExceptionData>().setAction(SocketAction4Android.ACTION_UPLOAD_EXCEPTION).setData(appExceptionData));
//            }
//
//            @Override
//            public void onSubmit() {
//                restartApplication();
//            }
//        });
    }

    //TODO 重启应用
    private void restartApplication() {
        closeAllThread();
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void exit() {
        //TODO 关闭线程，这里你应该使用线程池去管理所有线程，或者有一个线程的list存放，让在这里才能实现所有线程的统一关闭
        closeAllThread();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private void closeAllThread() {
        getCurrentActivity().finish();

    }


    //// TODO: 2016/7/11 获取版本号
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public void logout() {
    }

    @Override
    public void reset() {
    }


}
