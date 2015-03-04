package com.jincaizi.kuaiwin.mylottery;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

public class Register extends Activity implements OnClickListener, OnCheckedChangeListener {
	private static final String TAG = "Register";
	public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	private LinearLayout mLvRegister;
	private ImageView mLeftMenu;
	private Button mSubmitRegBtn;
	private RelativeLayout mRvNetworkHint;
	private boolean isNetworkWorking = false;
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
					Log.d(TAG, "没有可用网络");
					isNetworkWorking = false;
				}
			}
		}
	};
	private EditText mEtUserName;
	private EditText mEtPwd;
	private EditText mEtPwdSure;
	private TextView mPwdSureHint;
	private ToggleButton mMonitorPwd;
	private ToggleButton mMonitorPwdSure;
	private JinCaiZiProgressDialog mProgressDialog;
	private RequestHandle myRequestHandle;
	private  Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		mActivity = this;
		_checkNetwork();
		_findViews();
		_setListener();

	}

	private void _checkNetwork() {
		// 注册广播接收器
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(CONNECTIVITY_CHANGE_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	private void _findViews() {
		mLvRegister = (LinearLayout) findViewById(R.id.lv_register);
		mLeftMenu = (ImageView) findViewById(R.id.touzhu_leftmenu);
		mSubmitRegBtn = (Button) findViewById(R.id.submit_reg);
		mRvNetworkHint = (RelativeLayout) findViewById(R.id.rv_network_hint);
		TextView title = (TextView) findViewById(R.id.current_lottery);
		title.setText("注    册");
		findViewById(R.id.right_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		
		mEtUserName = (EditText)findViewById(R.id.edit_username);
		mEtPwd = (EditText)findViewById(R.id.edit_pwd);
		mEtPwdSure = (EditText)findViewById(R.id.edit_pwd_sure);
		
		mPwdSureHint = (TextView)findViewById(R.id.verify_hint_view);
		
		mMonitorPwd = (ToggleButton)findViewById(R.id.monitor_pwd);
		mMonitorPwdSure = (ToggleButton)findViewById(R.id.monitor_pwdsure);

	}

	private void _setListener() {
		mLvRegister.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				InputMethodManager inputManager = (InputMethodManager) Register.this
						.getApplicationContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(Register.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				return false;
			}
		});
		mLeftMenu.setOnClickListener(this);
		mSubmitRegBtn.setOnClickListener(this);
		mRvNetworkHint.setOnClickListener(this);
		
		mEtUserName.addTextChangedListener(new MyUserNameWatcher());
		mEtPwd.addTextChangedListener(new MyPwdWatcher());
		mEtPwdSure.addTextChangedListener(new MyPwdSureWatcher());
		
		mMonitorPwd.setOnCheckedChangeListener(this);
		mMonitorPwdSure.setOnCheckedChangeListener(this);
	}

    class MyUserNameWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String editable = mEtUserName.getText().toString();   
	        String str = nameFilter(editable.toString()); 
	        if(!editable.equals(str)){ 
	        	mEtUserName.setText(str);
	        	Toast.makeText(Register.this, "请输入字母，数字或者汉字", Toast.LENGTH_SHORT).show();
	            //设置新的光标所在位置 
	        	mEtUserName.setSelection(str.length()); 
	        } 
		}
    	
    }
    
    class MyPwdWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
//			if(!TextUtils.isEmpty(s) && !_patternPwd(s.toString())) {
//				Toast.makeText(Register.this, "请输入字母或者数字", Toast.LENGTH_SHORT).show();
//				int selectionEnd = mEtPwd.getSelectionEnd();
//				s = s.replace(selectionEnd - 1, selectionEnd, "");
//				mEtPwd.setText(s);
//				
//			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			String editable = mEtPwd.getText().toString();   
	        String str = stringFilter(editable.toString()); 
	        if(!editable.equals(str)){ 
	        	mEtPwd.setText(str);
	        	Toast.makeText(Register.this, "请输入字母或者数字", Toast.LENGTH_SHORT).show();
	            //设置新的光标所在位置 
	        	mEtPwd.setSelection(str.length()); 
	        } 

		}
    	
    }
    
    class MyPwdSureWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (!TextUtils.isEmpty(s) && !s.toString().equals(mEtPwd.getText().toString())) {
				mPwdSureHint.setVisibility(View.VISIBLE);
            } else {
            	mPwdSureHint.setVisibility(View.INVISIBLE);
            }
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    private String nameFilter(String str) throws PatternSyntaxException{
    
		  // 只允许字母、汉字和数字        
        String   regEx  =  "[^a-zA-Z0-9\u4e00-\u9fa5]";                      
        Pattern   p   =   Pattern.compile(regEx);      
        Matcher   m   =   p.matcher(str);      
        return   m.replaceAll("").trim();  
    }
    public static String stringFilter(String str)throws PatternSyntaxException{      
        // 只允许字母和数字        
        String   regEx  =  "[^a-zA-Z0-9]";                      
        Pattern   p   =   Pattern.compile(regEx);      
        Matcher   m   =   p.matcher(str);      
        return   m.replaceAll("").trim();      
    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	public void onClick(View v) {
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
		case R.id.submit_reg:
			if(!isNetworkWorking) {
				Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
				break;
			}
	        String anotherUser = null;
	        String anotherPwd = null;
	        try {
	        	anotherUser = new String(mEtUserName.getText().toString().getBytes("GBK"), "ISO8859_1");
	        	anotherPwd = new String(mEtPwd.getText().toString().getBytes("GBK"), "ISO8859_1");
	        	}
	        catch (UnsupportedEncodingException ex) {
	        	
	        }
			if(anotherUser.length() <4) {
				Toast.makeText(this, "用户名长度不能小于4位", Toast.LENGTH_SHORT).show();
				break;
			} else if(anotherUser.length() > 12) {
				Toast.makeText(this, "用户名长度不能大于12位", Toast.LENGTH_SHORT).show();
				break;
			} else if(anotherPwd.length() < 6)  {
				Toast.makeText(this, "密码长度不能小于6位", Toast.LENGTH_SHORT).show();
				break;
			} else if(anotherPwd.length() > 15) {
				Toast.makeText(this, "密码长度不能大于15位", Toast.LENGTH_SHORT).show();
				break;
			} else if(!mEtPwd.getText().toString().equals(mEtPwdSure.getText().toString())) {
				Toast.makeText(this, "登录密码与确认密码不一致！", Toast.LENGTH_SHORT).show();
				break;
			}
			_showProgress();
			_requestData();
			break;
		default:
			break;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch(buttonView.getId()) {
		case R.id.monitor_pwd:
			if(isChecked) {
				mEtPwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			} else {
				mEtPwd.setInputType(InputType.TYPE_CLASS_TEXT);
			}
			break;
		case R.id.monitor_pwdsure:
			if(isChecked) {
				mEtPwdSure.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			} else {
				mEtPwdSure.setInputType(InputType.TYPE_CLASS_TEXT);
			}
			break;
		default:
				break;
		}
		
	}
	
	
	private  void _requestData() {
		RequestParams params = new RequestParams();
	    params.add("act", "register");
	    params.add("username", mEtUserName.getText().toString());
	    params.add("password", mEtPwdSure.getText().toString());
		params.add("datatype", "json");
		params.add("jsoncallback",
				"jsonp" + String.valueOf(System.currentTimeMillis()));
		params.add("_", String.valueOf(System.currentTimeMillis()));
		String url = AsyncHttpClient.getUrlWithQueryString(false,
				JinCaiZiHttpClient.BASE_URL, params);
		myRequestHandle = JinCaiZiHttpClient.post(this, url,
				new AsyncHttpResponseHandler() {
					

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						String msg = "注册失败";
						int result = -1;
						JsonReader reader = null;
						try {
							String charset;
							if(Utils.isCmwapNet(Register.this)) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "竞彩场次 detail = " + jsonData);
							reader = new JsonReader(new StringReader(jsonData));
                            reader.beginObject();
                            while(reader.hasNext()) {
                            	String tagName = reader.nextName();
                            	if(tagName.equals("msg")) {
                            		msg = reader.nextString();
                            	} else if(tagName.equals("result")) {
                            		result = reader.nextInt();
                            	}
                            }
                            reader.endObject();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
							
						} finally {
							_hideProgress();
							if(result == 0) {
								Toast.makeText(Register.this, msg, Toast.LENGTH_SHORT).show();
								mActivity.setResult(RESULT_OK);
								mActivity.finish();
							} else {
								Toast.makeText(Register.this, msg, Toast.LENGTH_SHORT).show();
							}
							try {
								if(reader != null) {
								    reader.close();
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						//Log.d(TAG, "failure = " + error.toString());
						_hideProgress();
						Toast.makeText(Register.this, "注册失败", Toast.LENGTH_SHORT).show();
					}
				});
	}
	private void _showProgress() {
		mProgressDialog = JinCaiZiProgressDialog.show(this, "正在注册，请稍等");
		mProgressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				try {
					Log.d(TAG, "主动取消");
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
}
