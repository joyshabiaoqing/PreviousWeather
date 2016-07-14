package abc;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by Administrator on 2016/7/6.
 */
public abstract class FrameApplication extends Application {
    private Stack<FrameActivity> activityStack;
    private HashMap<String, FrameActivity> activityMap;
    private int width;
    private int height;

    public FrameApplication() {
    }

    public void onCreate() {
        super.onCreate();
        WindowManager windowManager = (WindowManager) this.getSystemService("window");
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.width = size.x;
        this.height = size.y;
        this.activityMap = new HashMap();

        this.activityStack = new Stack();
    }

    public void reset() {
    }

    public abstract void exit();

    public FrameActivity setCurrentActivity(FrameActivity activity) {
        this.activityMap.put(activity.getClass().getName(), activity);
        return (FrameActivity) this.activityStack.push(activity);
    }

    public FrameActivity getCurrentActivity() {
        if (!this.activityStack.isEmpty()) {
            return (FrameActivity) this.activityStack.peek();
        } else {
            this.exit();
            return null;
        }
    }

    public FrameActivity removeCurrentActivity() {
        FrameActivity activity = (FrameActivity) this.activityStack.pop();
        this.activityMap.remove(activity);
        return activity;
    }

    public void removeActivity(FrameActivity activity) {
        this.activityStack.remove(activity);
        this.activityMap.remove(activity);
    }

    public FrameActivity getTargetActivity(Class<? extends FrameActivity> targetClass) {
        return (FrameActivity) this.activityMap.get(targetClass.getName());
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isBackground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List appProcesses = activityManager.getRunningAppProcesses();
        Iterator var3 = appProcesses.iterator();

        ActivityManager.RunningAppProcessInfo appProcess;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            appProcess = (ActivityManager.RunningAppProcessInfo) var3.next();
        } while (!appProcess.processName.equals(this.getPackageName()));

        if (appProcess.importance == 400) {
            return true;
        } else {
            return false;
        }
    }
}
