package com.jincaizi.kuaiwin.tool;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CheckLogin {
     public static 	 void clearLoginStatus(SharedPreferences loginSp) {
 		//SharedPreferences loginSp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
 		Editor editor = loginSp.edit();
 		editor.putInt("userid", 0);
 		editor.putString("username", "");
 		editor.putString("upk", "");
 		editor.commit();
 	}
}
