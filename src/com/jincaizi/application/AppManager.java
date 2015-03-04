package com.jincaizi.application;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentLinkedQueue;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppManager {

	private static final String TAG = "AppManager";

	//private static Stack<Activity> activityStack;
	private static ConcurrentLinkedQueue<Activity>activityStack;

	// private static AppManager instance;
	/**
	 * private的构造函数用于避免外界直接使用new来实例化对象
	 */
	private AppManager() {
	}

	private static class SingletonHolder {
		/**
		 * 单例对象实例
		 */
		static final AppManager instance = new AppManager();
	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager() {
		return SingletonHolder.instance;
	}

	/**
	 * readResolve方法应对单例对象被序列化时候
	 */
	private Object readResolve() {
		return getAppManager();
	}

	/**
	 * 添加Activity到堆栈
	 */
	public synchronized void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new ConcurrentLinkedQueue<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public synchronized Activity currentActivity(){
		if(activityStack == null) {
			activityStack = new ConcurrentLinkedQueue<Activity>();
		}
		Activity activity = null;
		try {
		   activity = activityStack.poll();
		}catch(Exception e) {
			
		}
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public synchronized void finishActivity() {
		if(activityStack == null) {
			return;
		}
		Activity activity = activityStack.poll();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public synchronized void finishActivity(Activity activity) {
		if(activityStack == null) {
			return;
		}
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public synchronized void finishActivity(Class<?> cls) {
		if(activityStack == null) {
			return;
		}
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public synchronized void finishAllActivity() {
		if(activityStack == null) {
			return;
		}
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			Activity activity = activityStack.poll();
			if (null != activity) {
				activity.finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	@TargetApi(Build.VERSION_CODES.FROYO)
	public void appExit(Context context) {
		try {
			//finishAllActivity();
			finishAllActvitys(context);
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			String packName = context.getPackageName();
			// activityMgr.restartPackage(context.getPackageName());
			activityMgr.killBackgroundProcesses(packName);
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10);
			// activityMgr.killBackgroundProcesses(f.class.getPackage().getName());
			// System.exit(0);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
    
    private void finishAllActvitys(Context context) {
    	   Method method = null; 
           ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
           try {
            method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
            method.invoke(manager,context.getPackageName());
           } catch (Exception e) {
            Log.d("force",e.getMessage());    
           }

    }
}
