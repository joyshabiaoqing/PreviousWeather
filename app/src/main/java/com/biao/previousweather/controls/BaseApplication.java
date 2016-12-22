package com.biao.previousweather.controls;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.biao.previousweather.controls.component.RetrofitSingleton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class BaseApplication extends Application {

    public static String cacheDir = "";
    public static String patchPath = "";
    public static Context mAppContext = null;
    private static final String DIR = "rocoo_opt";
    private static File mOptDir;
    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        // 初始化 retrofit
        RetrofitSingleton.init();
//        CrashHandler.init(new CrashHandler(getApplicationContext()));
//        CrashReport.initCrashReport(getApplicationContext(), "900028220", false);
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();
//        LeakCanary.install(this);
//        RxUtils.unifiedErrorHandler();
//        Thread.setDefaultUncaughtExceptionHandler(new MyUnCaughtExceptionHandler());
        /**
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
         */
        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
            cacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            cacheDir = getApplicationContext().getCacheDir().toString();
        }
        if (ExistSDCard())
            patchPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        else {
            patchPath = Environment.getDataDirectory().getAbsolutePath();
        }
        patchPath = patchPath + "/previousweather";
        File file = new File(patchPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static Context getmAppContext() {
        return mAppContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        RocooFix.init(this);

//        File dexDir = new File(this.getFilesDir(), "hotfix");
//        dexDir.mkdir();
//        mOptDir = new File(this.getFilesDir(), DIR);
//        if (!mOptDir.exists() && !mOptDir.mkdirs()) {// make directory fail
//        }
//        String dexPath = null;
//        try {
//            dexPath = copyAsset(this, "patch.jar", dexDir);
//        } catch (IOException e) {
//        } finally {
//            if (dexPath != null && new File(dexPath).exists()) {
//                Log.e("==>",dexPath);
//            }
//        }
//        RocooFix.initPathFromAssets(this, "patch.jar");
//        RocooFix.applyPatch(this, patchPath + "/patch.jar");
    }

    public static String copyAsset(Context context, String assetName, File dir) throws IOException {
        File outFile = new File(dir, assetName);
        if (!outFile.exists()) {
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open(assetName);
            OutputStream out = new FileOutputStream(outFile);
            copyFile(in, out);
            in.close();
            out.close();
        }
        return outFile.getAbsolutePath();
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
