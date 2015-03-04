package com.jincaizi.kuaiwin.mylottery;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.tool.CheckLogin;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

public class Login extends Activity implements OnClickListener {
	public static final String TAG = "Login";
	public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	private static final int REGISTER = 5;
	private Button register, login;
	private EditText username, password;
	private SharedPreferences sp = null;
	private CheckBox auto = null;
	private JinCaiZiProgressDialog mProgressDialog;
	private RequestHandle myRequestHandle;
	private ImageView leftMenu;
	private boolean isNetworkWorking = false;
	private Activity mActivity;
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		private ConnectivityManager connectivityManager;
		private NetworkInfo info;

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CONNECTIVITY_CHANGE_ACTION)) {
				Log.d(TAG, "网络状态已经改变");
				connectivityManager = (ConnectivityManager)

				getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					String name = info.getTypeName();
					mRvNetworkHint.setVisibility(View.GONE);
					Log.d(TAG, "当前网络名称：" + name);
					isNetworkWorking = true;
				} else {
					mRvNetworkHint.setVisibility(View.VISIBLE);
					isNetworkWorking = false;
					Log.d(TAG, "没有可用网络");
				}
			}
		}
	};
	private RelativeLayout mRvNetworkHint;
	private SharedPreferences loginSp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mActivity = this;
		_checkNetwork();
		_findViews();
		_setListener();
		loginSp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		if (sp.getBoolean("auto", false)) {
			username.setText(sp.getString("uname", null));
			password.setText(sp.getString("upswd", null));
			auto.setChecked(true);
		}
		mProgressDialog = new JinCaiZiProgressDialog(this);
	}

	private void _checkNetwork() {
		// 注册广播接收器
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(CONNECTIVITY_CHANGE_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	private void _findViews() {
		register = (Button) findViewById(R.id.btn_register);
		login = (Button) findViewById(R.id.submit_login);
		username = (EditText) findViewById(R.id.longin_username);
		password = (EditText) findViewById(R.id.longin_pwd);
		auto = (CheckBox) findViewById(R.id.login_chx_savePsw);
		leftMenu = (ImageView) findViewById(R.id.touzhu_leftmenu);
		mRvNetworkHint = (RelativeLayout) findViewById(R.id.rv_network_hint);
		TextView title = (TextView) findViewById(R.id.current_lottery);
		title.setText("登    录");
		findViewById(R.id.right_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);

	}

	private void _setListener() {
		register.setOnClickListener(this);
		login.setOnClickListener(this);
		leftMenu.setOnClickListener(this);
		mRvNetworkHint.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rv_network_hint:
			Intent mIntent = new Intent("/");
			ComponentName comp = new ComponentName("com.android.settings",
					"com.android.settings.WirelessSettings");
			mIntent.setComponent(comp);
			mIntent.setAction("android.intent.action.VIEW");
			startActivityForResult(mIntent, 0);
			break;
		case R.id.touzhu_leftmenu:
			finish();
			break;
		case R.id.btn_register:
			Intent intent1 = new Intent(this, Register.class);
			startActivityForResult(intent1, REGISTER);
			break;
		case R.id.submit_login:
			if(!isNetworkWorking) {
				Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
				break;
			}
			if (TextUtils.isEmpty(username.getText())
					|| TextUtils.isEmpty(password.getText())) {
				Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
				break;
			}
			CheckLogin.clearLoginStatus(loginSp);
			final String name = username.getText().toString();
			final String pswd = password.getText().toString();
			final boolean autoLogin = auto.isChecked();
			_showProgress();
			RequestParams params = new RequestParams();
			params.add("act", "login");
			params.add("datatype", "json");
			params.add("username", name);
			params.add("password", pswd);
			params.add("jsoncallback",
					"jsonp" + String.valueOf(System.currentTimeMillis()));
			params.add("_", String.valueOf(System.currentTimeMillis()));
			String url = AsyncHttpClient.getUrlWithQueryString(false,
					JinCaiZiHttpClient.BASE_URL, params);
			JinCaiZiHttpClient.post(this, url, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						// Object jsonResponse = parseResponse(responseBody);
						String charset;
						if(Utils.isCmwapNet(Login.this)) {
							charset = "utf-8";
						} else {
							charset = "gb2312";
						}
						String jsonData = new String(responseBody, charset);
						Log.d(TAG, "success = body" + jsonData);
						// 如果需要解析JSON数据，首要要生成一个JsonReader对象
						String returnName = "";
						int returnUserid = 0;
						int returnResult = -2;
						String upk ="";
						JsonReader reader = new JsonReader(new StringReader(
								jsonData));
						reader.beginObject();
						while (reader.hasNext()) {
							String tagName = reader.nextName();
							if (tagName.equals("msg")) {
								returnName = reader.nextString();
								Log.d(TAG, "name--->" + returnName);
								
							} else if (tagName.equals("userid")) {
								returnUserid = reader.nextInt();
								Log.d(TAG, "userid--->" + returnUserid);
								
							} else if(tagName.equals("upk")) {
								upk = reader.nextString();
							} else if (tagName.equals("result")) {
								returnResult = reader.nextInt();
								Log.d(TAG, "result--->" + returnResult);
								
							}
						}
						reader.endObject();
						reader.close();
						if (returnResult != 0) {
							Toast.makeText(mActivity, returnName,
									Toast.LENGTH_SHORT).show();
						} else {
							_storeLoginStatus(returnUserid, name, upk);
							if (autoLogin) {
								Editor editor = sp.edit();
								editor.putString("uname", name);
								editor.putString("upswd", pswd);
								editor.putBoolean("auto", true);
								editor.commit();
							} else {
								Editor editor = sp.edit();
								editor.putString("uname", null);
								editor.putString("upswd", null);
								editor.putBoolean("auto", false);
								editor.commit();
							}
							_hideProgress();
							mActivity.setResult(RESULT_OK);
						
							finish();
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						_hideProgress();
					}
					
					
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					Log.d(TAG, "failure = " + error.toString());
					_hideProgress();
					Toast.makeText(mActivity, "登录失败， 请重试", Toast.LENGTH_SHORT).show();
				}
			});
			
			break;

		}
	}

	protected void _showProgress() {
		mProgressDialog = JinCaiZiProgressDialog.show(this, "正在登陆......");
		mProgressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				try {
					Log.d(TAG, "主动取消登陆");
					if (myRequestHandle != null) {
						myRequestHandle.cancel(true);
					}
				} catch (Exception e) {
					Log.e(TAG, "无效返回", e);
				} finally {
				}
			}
		});
	}

	private void _hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	private void _storeLoginStatus(int userid, String userName, String upk) {
		
		Editor editor = loginSp.edit();
		editor.putInt("userid", userid);
		editor.putString("username", userName);
		editor.putString("upk", upk);
		editor.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}
