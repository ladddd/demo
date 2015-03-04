package com.jincaizi.kuaiwin.mylottery;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.stream.JsonReader;
import com.jincaizi.R;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.tool.CheckLogin;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;

public class PhoneAuth extends Activity implements OnClickListener {
	private static final String TAG = "PhoneAuth";
	private SharedPreferences sp;
	private int userid;
	private String mUpk;
	private String userName;
	private ImageView mLeftBack;
	private EditText mIdNumView;
	private TextView userNameView;
	private Button mAuthButton;
	private Dialog localDialog;
	private EditText inputSmsCodeView;
	private JinCaiZiProgressDialog mProgressDialog;

	private Activity mActivity;
	private boolean isSuccess = false;
	private TextView mAuthHintView;
	private boolean isAuth = false;
	private EditText inputNewPhoneView;
	private EditText inputNewSmsCodeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.realname_auth);
		mActivity = this;
		sp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		_findViews();
		userid = sp.getInt("userid", 0);
		mUpk = sp.getString("upk", "");
		userName = sp.getString("username", "");
		userNameView.setText(userName);
		userNameView.setTextColor(getResources().getColor(android.R.color.darker_gray));
		_setListener();
		_getPhoneAuth();
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			userid = sp.getInt("userid", 0);
			mUpk = sp.getString("upk", "");
			userName = sp.getString("username", "");
			userNameView.setText(userName);
			userNameView.setTextColor(getResources().getColor(android.R.color.darker_gray));
			_getPhoneAuth();
		}
		
	}
    private void _findViews() {
    	mLeftBack = (ImageView)findViewById(R.id.touzhu_leftmenu);
    	TextView titleView = (TextView)findViewById(R.id.current_lottery);
    	titleView.setText("手机认证");
    	titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    	findViewById(R.id.mid_relativelayout).setVisibility(View.GONE);
    	TextView phoneNumView = (TextView)findViewById(R.id.identity_hint);
    	phoneNumView.setText("手机号码");
    	
    	userNameView = (TextView)findViewById(R.id.username_login);
    	
    	
    	mIdNumView = (EditText)findViewById(R.id.identity_id);
    	mIdNumView.setInputType(InputType.TYPE_CLASS_NUMBER);
    	mIdNumView.setHint("请输入手机号码");
    	
    	mAuthButton = (Button)findViewById(R.id.submit_auth);
    	mAuthButton.setText("手机验证");
    	
    	mAuthHintView = (TextView)findViewById(R.id.auth_hint);
    }
    
    private void _setListener() {
    	mLeftBack.setOnClickListener(this);
    	mAuthButton.setOnClickListener(this);
    	mAuthHintView.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.touzhu_leftmenu:
			finish();
			break;
		case R.id.submit_auth:
			if(TextUtils.isEmpty(mIdNumView.getText())) {
				Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
				break;
			}
			if(!Utils.isMobileNO(mIdNumView.getText().toString().trim())) {
				Toast.makeText(this, "请检查手机号码输入格式是否正确", Toast.LENGTH_SHORT).show();
				break;
			}
			if(!isSuccess) {
				Toast.makeText(this, "请先获取认证消息以确认是否已经绑定手机", Toast.LENGTH_SHORT).show();
				break;
			}
			if(isAuth) {
				_showModifyAuthDialog();
			} else {
			    _showAuthDialog();
			}
			break;
		case R.id.getSmsCode:
			Log.d("test", "getSmsCode");
			Toast.makeText(this, "已发送获取请求", Toast.LENGTH_SHORT).show();
			_getSmsCode(mIdNumView.getText().toString().trim());
			break;
		case R.id.phone_auth_submit:
			if(TextUtils.isEmpty(inputSmsCodeView.getText())) {
				Toast.makeText(this, "短信验证码不能为空", Toast.LENGTH_SHORT).show();
				break;
			}
			//localDialog.dismiss();
			Log.d("test", "doPhoneAuth");
			_doPhoneAuth(inputSmsCodeView.getText().toString().trim(), mIdNumView.getText().toString().trim());
			break;
		case R.id.auth_hint:
			if(!isSuccess) {
				_getPhoneAuth();
			}
			break;
		case R.id.getEmailCode://修改手机绑定手机，重新获取短信码
			if(TextUtils.isEmpty(inputNewPhoneView.getText())) {
				Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
				break;
			}
			if(!Utils.isMobileNO(inputNewPhoneView.getText().toString().trim())) {
				Toast.makeText(this, "请检查手机号码输入格式是否正确", Toast.LENGTH_SHORT).show();
				break;
			}
			Toast.makeText(this, "已发送获取请求", Toast.LENGTH_SHORT).show();
			_getSmsCode(inputNewPhoneView.getText().toString().trim());
			break;
		case R.id.email_auth_submit://修改手机绑定手机，重新提交绑定请求
			if(TextUtils.isEmpty(inputNewSmsCodeView.getText())) {
				Toast.makeText(this, "短信验证码不能为空", Toast.LENGTH_SHORT).show();
				break;
			}
			//localDialog.dismiss();
			Log.d("test", "doPhoneAuth");
			_doPhoneAuth(inputNewSmsCodeView.getText().toString().trim(), inputNewPhoneView.getText().toString().trim());
			break;
		default:
			break;
			
		}
	}
	private void _showAuthDialog() {
		 localDialog = new Dialog(PhoneAuth.this, R.style.Theme_dialog);
			View view = LayoutInflater.from(getApplicationContext()).inflate(
						R.layout.auth_dialog, null);
			TextView getSmsCodeView = (TextView)view.findViewById(R.id.getSmsCode);
			getSmsCodeView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
			getSmsCodeView.setOnClickListener(this);
			
			inputSmsCodeView = (EditText)view.findViewById(R.id.inputSmsCode);
			
			TextView authSubmitView = (TextView)view.findViewById(R.id.phone_auth_submit);
			authSubmitView.setOnClickListener(this);
			
			localDialog.setContentView(view);
			Window dialogWindow = localDialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = 400; // 宽度
			dialogWindow.setAttributes(lp);
			localDialog.show();
			
	}
	private void _getPhoneAuth() {
		_showProgress("正在获取认证信息");
		RequestParams params = new RequestParams();
		params.add("act", "bindmobile");
		params.add("type", "0");
		params.add("userid", userid+"");
		params.add("upk", mUpk);
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
					isSuccess = true;
					String charset;
					if(Utils.isCmwapNet(PhoneAuth.this)) {
						charset = "utf-8";
					} else {
						charset = "gb2312";
					}
					String jsonData = new String(responseBody, charset);
					Log.d(TAG, "success = body" + jsonData);
					// 如果需要解析JSON数据，首要要生成一个JsonReader对象
					String returnName = "";
					String phoneNumber = "";
					int returnResult = -1;
					JsonReader reader = new JsonReader(new StringReader(
							jsonData));
					reader.beginObject();
					while (reader.hasNext()) {
						String tagName = reader.nextName();
						if (tagName.equals("msg")) {
							returnName = reader.nextString();
							Log.d(TAG, "name--->" + returnName);
							
						} else if (tagName.equals("result")) {
							returnResult = reader.nextInt();
							Log.d(TAG, "result--->" + returnResult);
							
						} else if(tagName.equals("mobile")) {
							phoneNumber = reader.nextString();
						} 
					}
					reader.endObject();
					reader.close();
					if(returnResult == -2) {
						CheckLogin.clearLoginStatus(sp);
						 Intent loginIntent = new Intent(PhoneAuth.this, Login.class);
						 startActivityForResult(loginIntent, 2);
					}else if (returnResult == -1) {
						Toast.makeText(mActivity, returnName,
								Toast.LENGTH_SHORT).show();
					} else if (returnResult == 1) {
						mAuthHintView.setText(returnName);
					}else if(returnResult == 0){	
						isAuth = true;
						mAuthHintView.setText(returnName);
						mIdNumView.setText(phoneNumber);
						mIdNumView.setEnabled(false);
						mAuthButton.setText("修改手机");
					}
					_hideProgress();
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
				isSuccess = false;
				_hideProgress();
				Toast.makeText(mActivity, "获取失败", Toast.LENGTH_SHORT).show();
				mAuthHintView.setText("点击此处重新获取认证信息");
			}
		});
	}
	private void _getSmsCode(String mobile) {
		_showProgress("正在提交获取短信码请求");
		RequestParams paramsUrl = new RequestParams();
		paramsUrl.add("act", "bindmobile");
		paramsUrl.add("type", "1");
		paramsUrl.add("userid", userid+"");
		paramsUrl.add("upk", mUpk);
		paramsUrl.add("jsoncallback",
				"jsonp" + String.valueOf(System.currentTimeMillis()));
		paramsUrl.add("_", String.valueOf(System.currentTimeMillis()));
		String url = AsyncHttpClient.getUrlWithQueryString(false,
				JinCaiZiHttpClient.BASE_URL, paramsUrl);
		RequestParams rawParams = new RequestParams();
		rawParams.add("mobile", mobile);
		JinCaiZiHttpClient.postFormData(this, url, rawParams, new AsyncHttpResponseHandler() {
			private String responseMsg;

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					String charset;
					if(Utils.isCmwapNet(PhoneAuth.this)) {
						charset = "utf-8";
					} else {
						charset = "gb2312";
					}
					String jsonData = new String(responseBody, charset);
					Log.d(TAG, "post groupbuy form data = " + jsonData);
					String returnName = "";
					int returnResult = -1;
					JsonReader reader = new JsonReader(new StringReader(
							jsonData));
					reader.beginObject();
					while (reader.hasNext()) {
						String tagName = reader.nextName();
						if (tagName.equals("msg")) {
							returnName = reader.nextString();
							Log.d(TAG, "name--->" + returnName);
							
						} else if (tagName.equals("result")) {
							returnResult = reader.nextInt();
							Log.d(TAG, "result--->" + returnResult);
							
						} 
					}
					reader.endObject();
					reader.close();
					if(returnResult == -2) {
						CheckLogin.clearLoginStatus(sp);
						 Intent loginIntent = new Intent(PhoneAuth.this, Login.class);
						 startActivityForResult(loginIntent, 2);
					}else if (returnResult == -1) {
						Toast.makeText(mActivity, returnName,
								Toast.LENGTH_SHORT).show();
					} else if (returnResult == 1) {
						Toast.makeText(mActivity, returnName,
								Toast.LENGTH_SHORT).show();
					}else if(returnResult == 0){	
						Toast.makeText(mActivity, returnName,
								Toast.LENGTH_SHORT).show();
					}
					_hideProgress();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					//responseMsg = "短信码获取失败！";
				} catch (Exception e) {
					e.printStackTrace();
					//responseMsg = "短信码获取失败！";
				} finally {
					
				}
				_hideProgress();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				//Log.d(TAG, "failure = " + error.toString());
				_hideProgress();
				responseMsg = "短信码请求失败！，请重新提交！";
				Toast.makeText(PhoneAuth.this, responseMsg, Toast.LENGTH_SHORT).show();
				//finish();
			}
		});  
	}
	private void _doPhoneAuth(String smsCode, final String phoneNumStr) {
		_showProgress("正在提交认证信息");
		RequestParams paramsUrl = new RequestParams();
		paramsUrl.add("act", "bindmobile");
		paramsUrl.add("type", "2");
		paramsUrl.add("userid", userid+"");
		paramsUrl.add("upk", mUpk);
		paramsUrl.add("jsoncallback",
				"jsonp" + String.valueOf(System.currentTimeMillis()));
		paramsUrl.add("_", String.valueOf(System.currentTimeMillis()));
		String url = AsyncHttpClient.getUrlWithQueryString(false,
				JinCaiZiHttpClient.BASE_URL, paramsUrl);
		RequestParams rawParams = new RequestParams();
		rawParams.add("code", smsCode);
		JinCaiZiHttpClient.postFormData(this, url, rawParams, new AsyncHttpResponseHandler() {
			private String responseMsg;

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					localDialog.dismiss();
					String charset;
					if(Utils.isCmwapNet(PhoneAuth.this)) {
						charset = "utf-8";
					} else {
						charset = "gb2312";
					}
					String jsonData = new String(responseBody, charset);
					Log.d(TAG, "post groupbuy form data = " + jsonData);
					String returnName = "";
					int returnResult = -1;
					JsonReader reader = new JsonReader(new StringReader(
							jsonData));
					reader.beginObject();
					while (reader.hasNext()) {
						String tagName = reader.nextName();
						if (tagName.equals("msg")) {
							returnName = reader.nextString();
							Log.d(TAG, "name--->" + returnName);
							
						} else if (tagName.equals("result")) {
							returnResult = reader.nextInt();
							Log.d(TAG, "result--->" + returnResult);
							
						} 
					}
					reader.endObject();
					reader.close();
					if(returnResult == -2) {
						CheckLogin.clearLoginStatus(sp);
						 Intent loginIntent = new Intent(PhoneAuth.this, Login.class);
						 startActivityForResult(loginIntent, 2);
					}else if (returnResult == -1) {
						Toast.makeText(mActivity, returnName,
								Toast.LENGTH_SHORT).show();
					} else if (returnResult == 1) {
						mAuthHintView.setText(returnName);
					}else if(returnResult == 0){	
						isAuth = true;
						mAuthHintView.setText(returnName);
						mIdNumView.setEnabled(false);
						mIdNumView.setText(phoneNumStr);
						mAuthButton.setText("修改手机");
					}
					_hideProgress();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					//responseMsg = "短信码获取失败！";
				} catch (Exception e) {
					e.printStackTrace();
					//responseMsg = "短信码获取失败！";
				} finally {
					
				}
				_hideProgress();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				//Log.d(TAG, "failure = " + error.toString());
				_hideProgress();
				responseMsg = "认证请求失败！，请重新提交！";
				Toast.makeText(PhoneAuth.this, responseMsg, Toast.LENGTH_SHORT).show();
				//finish();
			}
		});  
	}
	protected void _showProgress(String  str) {
		mProgressDialog = JinCaiZiProgressDialog.show(this, str);
	}

	private void _hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}
	private void _showModifyAuthDialog() {
		 localDialog = new Dialog(PhoneAuth.this, R.style.Theme_dialog);
			View view = LayoutInflater.from(getApplicationContext()).inflate(
						R.layout.email_auth_dialog, null);
			TextView dialogTitle = (TextView)view.findViewById(R.id.email_auth_title);
			dialogTitle.setText("绑定手机");
			TextView stepFirstHint = (TextView)view.findViewById(R.id.email_firststep_hint);
			TextView stepSecondHint = (TextView)view.findViewById(R.id.email_secondstep_hint);
			stepFirstHint.setText("第一步：输入新的手机号码：");
			stepSecondHint.setText("第二步：输入短信验证码：");
			inputNewPhoneView = (EditText)view.findViewById(R.id.inputNewEmail);
			TextView getEmailCodeView = (TextView)view.findViewById(R.id.getEmailCode);
			getEmailCodeView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
			getEmailCodeView.setOnClickListener(this);
			
			inputNewSmsCodeView = (EditText)view.findViewById(R.id.inputEmailCode);
			
			TextView authSubmitView = (TextView)view.findViewById(R.id.email_auth_submit);
			authSubmitView.setOnClickListener(this);
			
			localDialog.setContentView(view);
			Window dialogWindow = localDialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = 400; // 宽度
			dialogWindow.setAttributes(lp);
			localDialog.show();
			
	}
}
