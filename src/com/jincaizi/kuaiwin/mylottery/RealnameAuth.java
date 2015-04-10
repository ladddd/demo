package com.jincaizi.kuaiwin.mylottery;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import android.widget.*;
import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.stream.JsonReader;
import com.jincaizi.R;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.tool.CheckLogin;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;

public class RealnameAuth extends Activity implements OnClickListener{
	private static final String TAG = "RealnameAuth";
	private SharedPreferences sp;
	private int userid;
	private String mUpk;
	private String userName;
	private RelativeLayout mLeftBack;
	private TextView userNameView;
	private Button mAuthButton;
	private EditText mRealNameView;
	private EditText mIdentityView;
	private JinCaiZiProgressDialog mProgressDialog;
	private Activity mActivity;
	private TextView mAuthHintView;
	private boolean isSuccess = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.realname_auth);
		sp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		_findViews();
		userid = sp.getInt("userid", 0);
		mUpk = sp.getString("upk", "");
		Log.d("test", "userid = " + userid + ";upk=" + mUpk);
		userName = sp.getString("username", "");
		userNameView.setText(userName);
		mActivity = this;
	
		_setListener();	
		_getAuthInfo();
	}

	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			userid = sp.getInt("userid", 0);
			mUpk = sp.getString("upk", "");
			Log.d("test", "userid = " + userid + ";upk=" + mUpk);
			userName = sp.getString("username", "");
			userNameView.setText(userName);
			_getAuthInfo();
		}
		
	}

	private void _findViews() {
    	mLeftBack = (RelativeLayout)findViewById(R.id.left_layout);
    	TextView titleView = (TextView)findViewById(R.id.current_lottery);
    	titleView.setText("实名认证");
    	titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    	
    	userNameView = (TextView)findViewById(R.id.username_login);
    	mAuthButton = (Button)findViewById(R.id.submit_auth);
    	mAuthButton.setText("实名认证");
    	
    	mRealNameView = (EditText)findViewById(R.id.realname_login);
    	mIdentityView = (EditText)findViewById(R.id.identity_id);
    	
    	mAuthHintView = (TextView)findViewById(R.id.auth_hint);
        findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
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
		case R.id.left_layout:
			finish();
			break;
		case R.id.submit_auth:
			if(TextUtils.isEmpty(mRealNameView.getText())) {
				Toast.makeText(this, "真实姓名不能为空", Toast.LENGTH_SHORT).show();
				break;
			}
			if(TextUtils.isEmpty(mIdentityView.getText())) {
				Toast.makeText(this, "身份证号不能为空", Toast.LENGTH_SHORT).show();
				break;
			}
			try {
				if(!Utils.IDCardValidate(mIdentityView.getText().toString().trim())) {
					Toast.makeText(this, "请检查身份证号是否正确", Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(this, "请检查身份证号是否正确", Toast.LENGTH_SHORT).show();
				break;
			}
			if(!isSuccess) {
				Toast.makeText(this, "请先获取认证消息以确认是否已经实名认证", Toast.LENGTH_SHORT).show();
				break;
			}
			_doAuth();
			break;
		case R.id.auth_hint:
			if(!isSuccess) {
				_getAuthInfo();
			}
			break;
		default:
			break;
			
		}
	}
	
	private void _getAuthInfo() {
		_showProgress("正在获取认证信息");
		RequestParams params = new RequestParams();
		params.add("act", "realname");
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
					isSuccess = true;
					String charset;
					if(Utils.isCmwapNet(RealnameAuth.this)) {
						charset = "utf-8";
					} else {
						charset = "gb2312";
					}
					String jsonData = new String(responseBody, charset);
					Log.d(TAG, "success = body" + jsonData);
					// 如果需要解析JSON数据，首要要生成一个JsonReader对象
					String returnName = "";
					String realName = "";
					String idNum = "";
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
							
						} else if(tagName.equals("realname")) {
							realName = reader.nextString();
						} else if(tagName.equals("idnumber")) {
							idNum = reader.nextString();
						}
					}
					reader.endObject();
					reader.close();
					if(returnResult == -2) {
						CheckLogin.clearLoginStatus(sp);
						 Intent loginIntent = new Intent(RealnameAuth.this, Login.class);
						 startActivityForResult(loginIntent, 2);
					}else if (returnResult == -1) {
						Toast.makeText(mActivity, returnName,
								Toast.LENGTH_SHORT).show();
					} else if (returnResult == 1) {
						mAuthHintView.setText(returnName);
                        mAuthHintView.setVisibility(View.VISIBLE);
					}else if(returnResult == 0){	
						mAuthHintView.setText(returnName);
                        mAuthHintView.setVisibility(View.VISIBLE);
						mRealNameView.setText(realName);
						mIdentityView.setText(idNum);
						mRealNameView.setEnabled(false);
						mIdentityView.setEnabled(false);
						mAuthButton.setVisibility(View.GONE);
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
                mAuthHintView.setVisibility(View.VISIBLE);
			}
		});
	}
	private void _doAuth() {
		_showProgress("正在提交认证信息");
		RequestParams paramsUrl = new RequestParams();
		paramsUrl.add("act", "realname");
		paramsUrl.add("type", "1");
		paramsUrl.add("userid", userid+"");
		paramsUrl.add("upk", mUpk);
		paramsUrl.add("jsoncallback",
				"jsonp" + String.valueOf(System.currentTimeMillis()));
		paramsUrl.add("_", String.valueOf(System.currentTimeMillis()));
		String url = AsyncHttpClient.getUrlWithQueryString(false,
				JinCaiZiHttpClient.BASE_URL, paramsUrl);
		RequestParams rawParams = new RequestParams();
		rawParams.add("realname", mRealNameView.getText().toString().trim());
		rawParams.add("idnumber", mIdentityView.getText().toString().trim());
		JinCaiZiHttpClient.postFormData(this, url, rawParams, new AsyncHttpResponseHandler() {
			private String responseMsg;

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					String charset;
					if(Utils.isCmwapNet(RealnameAuth.this)) {
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
						 Intent loginIntent = new Intent(RealnameAuth.this, Login.class);
						 startActivityForResult(loginIntent, 2);
					}else if (returnResult == -1) {
						Toast.makeText(mActivity, returnName,
								Toast.LENGTH_SHORT).show();
					} else if (returnResult == 1) {
						mAuthHintView.setText(returnName);
                        mAuthHintView.setVisibility(View.VISIBLE);
					}else if(returnResult == 0){	
						mAuthHintView.setText(returnName);
                        mAuthHintView.setVisibility(View.VISIBLE);
						String str = mIdentityView.getText().toString();
						mIdentityView.setText(str.substring(0, str.length() - 6) + "******");
						mRealNameView.setEnabled(false);
						mIdentityView.setEnabled(false);
						mAuthButton.setVisibility(View.GONE);
					}
					_hideProgress();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					responseMsg = "认证失败！";
				} catch (Exception e) {
					e.printStackTrace();
					responseMsg = "认证失败！";
				} finally {
					
				}
				_hideProgress();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				_hideProgress();
				responseMsg = "认证失败，请重新提交！";
				Toast.makeText(RealnameAuth.this, responseMsg, Toast.LENGTH_SHORT).show();
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
}
