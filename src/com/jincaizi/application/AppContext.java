package com.jincaizi.application;

import java.util.Locale;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

public class AppContext extends Application {

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;


    public static final int NETWORK_ANOMALY = 0x00;
    public static final int LOGIN_LOGGING = 0x01;
    public static final int LOGIN_LOGOUT = 0x02;
    public static final int LOGIN_FAILED = 0x03;
    public static final int LOGIN_SUCCESSFUL = 0x04;
    public int loginStatus = LOGIN_LOGGING;

    private static AppContext instance;

    public static synchronized AppContext getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.instance = this;
     // 注册App异常崩溃处理器
        //AppException crashHandler = AppException.getInstance();  
        //crashHandler.init(getApplicationContext());
       // Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }


    @Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

    /**
     
     * 
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

   
    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase(Locale.getDefault()).equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }


    /**
     * 获取App安装包信息
     * 
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) {
            info = new PackageInfo();
        }
        return info;
    }
}
