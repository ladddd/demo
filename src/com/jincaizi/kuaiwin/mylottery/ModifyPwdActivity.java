package com.jincaizi.kuaiwin.mylottery;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.application.AppContext;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

/**
 * 设置功能----修改密码
 *
 * @author yj
 */
public class ModifyPwdActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

    protected static final String TAG = "ModifyPwdActivity";
	private static final int LOGINREQUESTCODE = 4;
	private ImageView mBackBtn;
    private TextView mEditActivityTitle;
	private EditText mOldPwdView, mNewPwdView, mRepeatPwdView;
    private Button mOkBtn;
	private ToggleButton mMonitorOld;
	private ToggleButton mMonitorNew;
	private ToggleButton mMonitorNewSure;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_pwd_layout);
        _getUserId();
        _findViews();
        _setListener();
    }

    private void _findViews() {
        mBackBtn = (ImageView) findViewById(R.id.touzhu_leftmenu);
        mEditActivityTitle = (TextView) findViewById(R.id.current_lottery);
        mEditActivityTitle.setText("修改密码");
        mOldPwdView = (EditText) findViewById(R.id.old_pwd_view);
        mNewPwdView = (EditText) findViewById(R.id.new_pwd_view);
        mRepeatPwdView = (EditText) findViewById(R.id.repeat_pwd_view);
        mOkBtn = (Button) findViewById(R.id.modifypwd_ok_btn);
  	  findViewById(R.id.right_divider).setVisibility(View.GONE);
	  findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
	  mMonitorOld = (ToggleButton)findViewById(R.id.monitor_oldpwd);
	 // mMonitorOld.setHint("请输入登录密码");
	  mMonitorNew = (ToggleButton)findViewById(R.id.monitor_newpwd);
	  //mMonitorNew.setHint("6-15位字母，数字" );
	  mMonitorNewSure = (ToggleButton)findViewById(R.id.monitor_newpwdsure);
	  //mMonitorNewSure.setHint("请再次输入新密码");
    }

    private void _setListener() {
        mBackBtn.setOnClickListener(this);
        mOkBtn.setOnClickListener(this);
        mOldPwdView.addTextChangedListener(mNewPwdWatcher);
        mNewPwdView.addTextChangedListener(mNewPwdWatcher);
        mRepeatPwdView.addTextChangedListener(mRepeatPwdWatcher);
        mMonitorOld.setOnCheckedChangeListener(this);
        mMonitorNew.setOnCheckedChangeListener(this);
        mMonitorNewSure.setOnCheckedChangeListener(this);
    }
    private final TextWatcher mNewPwdWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO new pwd safety hint
        	String editable = mNewPwdView.getText().toString();   
	        String str = stringFilter(editable.toString()); 
	        if(!editable.equals(str)){ 
	        	mNewPwdView.setText(str);
	        	Toast.makeText(ModifyPwdActivity.this, "请输入字母或者数字", Toast.LENGTH_SHORT).show();
	            //设置新的光标所在位置 
	        	mNewPwdView.setSelection(str.length()); 
	        } 
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };
    public static String stringFilter(String str)throws PatternSyntaxException{      
        // 只允许字母和数字        
        String   regEx  =  "[^a-zA-Z0-9]";                      
        Pattern   p   =   Pattern.compile(regEx);      
        Matcher   m   =   p.matcher(str);      
        return   m.replaceAll("").trim();      
    }
    private final TextWatcher mRepeatPwdWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s) && !s.toString().equals(mNewPwdView.getText().toString())) {
                findViewById(R.id.verify_hint_view).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.verify_hint_view).setVisibility(View.INVISIBLE);
            }
        }
    };
	private RequestHandle myRequestHandle;
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.touzhu_leftmenu:
                finish();
                break;
            case R.id.modifypwd_ok_btn:
            	if(!AppContext.getInstance().isNetworkConnected()) {
            		Toast.makeText(getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
            		break;
            	}
            	if(TextUtils.isEmpty(mOldPwdView.getText())) {
            		Toast.makeText(getApplicationContext(), "请输入原密码", Toast.LENGTH_SHORT).show();
            		break;
            	}
            	if(TextUtils.isEmpty(mNewPwdView.getText())) {
            		Toast.makeText(getApplicationContext(), "请输入新密码", Toast.LENGTH_SHORT).show();
            		break;
            	}
            	if(TextUtils.isEmpty(mRepeatPwdView.getText())) {
            		Toast.makeText(getApplicationContext(), "请输入确认密码", Toast.LENGTH_SHORT).show();
            		break;
            	}
            	if(!mRepeatPwdView.getText().toString().equals(mNewPwdView.getText().toString())) {
            		Toast.makeText(getApplicationContext(), "新密码与确认密码不一致，请确认", Toast.LENGTH_SHORT).show();
            		break;
            	}
       	        String anotherPwd = null;
       	        try {
       	        	anotherPwd = new String(mNewPwdView.getText().toString().getBytes("GBK"), "ISO8859_1");
       	        	}
       	        catch (UnsupportedEncodingException ex) {
       	        	
       	        }
            	if(anotherPwd.length() > 15) {
            		Toast.makeText(getApplicationContext(), "密码长度不能超过15位", Toast.LENGTH_SHORT).show();
            		break;
            	}
            	if(anotherPwd.length() < 6) {
            		Toast.makeText(getApplicationContext(), "密码长度不能小于6位", Toast.LENGTH_SHORT).show();
            		break;
            	}
            	_doPostFormData();
                 break;
            default:
                break;

        }
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch(buttonView.getId()) {
		case R.id.monitor_oldpwd:
			if(isChecked) {
				mOldPwdView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			} else {
				mOldPwdView.setInputType(InputType.TYPE_CLASS_TEXT);
			}
			break;
		case R.id.monitor_newpwd:
			if(isChecked) {
				mNewPwdView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			} else {
				mNewPwdView.setInputType(InputType.TYPE_CLASS_TEXT);
			}
			break;
		case R.id.monitor_newpwdsure:
			if(isChecked) {
				mRepeatPwdView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			} else {
				mRepeatPwdView.setInputType(InputType.TYPE_CLASS_TEXT);
			}
			break;
		default:
				break;
		}
	}
	
	private void _doPostFormData() {
		_showProgress("正在提交，请稍等！");
		RequestParams params = new RequestParams();
		params.add("act", "userpwd");
		params.add("userid", mUserId);
		params.add("upk", mUpk);
		params.add("username", mUserName);
		params.add("oldpwd", mOldPwdView.getText().toString());
		params.add("newpwd", mNewPwdView.getText().toString());
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
						try {
							String charset;
							if(Utils.isCmwapNet(ModifyPwdActivity.this)) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "参与合买详情 detail = " + jsonData);
							JsonReader reader = new JsonReader(new StringReader(jsonData));
							reader.beginObject();
							while(reader.hasNext()) {
								String tagName = reader.nextName();
								if(tagName.equals("msg")) {
									modifyResponseMsg = reader.nextString();
								} else if(tagName.equals("result")) {
									modifyResponseResult = reader.nextInt();
								} 
							}
							reader.endObject();
							reader.close();
							
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							modifyResponseMsg = "密码修改失败";
						} catch (Exception e) {
							e.printStackTrace();
							modifyResponseMsg = "密码修改失败";
						} finally {
							 _hideProgress();
							 final Dialog localDialog = new Dialog(ModifyPwdActivity.this, R.style.Theme_dialog);
								View view = LayoutInflater.from(getApplicationContext()).inflate(
											R.layout.dialog_submit_bet, null);
									TextView dialogTitle = (TextView) view
											.findViewById(R.id.submit_dialog_title);
									dialogTitle.setText("提示");
									TextView dialogContent = (TextView) view
											.findViewById(R.id.submit_dialog_content);
									TextView dialogCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
									dialogCancel.setVisibility(View.GONE);
									//view.findViewById(R.id.dialog_divider).setVisibility(View.GONE);
									TextView dialogOK = (TextView) view.findViewById(R.id.tv_submit_ok);
									dialogOK.setText("确定");
									localDialog.setContentView(view);
									Window dialogWindow = localDialog.getWindow();
									WindowManager.LayoutParams lp = dialogWindow.getAttributes();
									lp.width = 300; // 宽度
									dialogWindow.setAttributes(lp);
									dialogOK.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											 localDialog.cancel();
											 if(modifyResponseResult == 0) {
											     finish();
											 }
										}
									});
									 dialogContent.setText(modifyResponseMsg);
									localDialog.show();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						Log.d(TAG, "failure = " + error.toString());
						_hideProgress();
						Toast.makeText(ModifyPwdActivity.this, "密码修改失败", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}
	private String modifyResponseMsg = "密码修改失败";
	private int modifyResponseResult = -1;
	private JinCaiZiProgressDialog mProgressDialog;
	private SharedPreferences sp;
	private String mUpk;
	private String mUserId;
	private String mUserName;
	protected void _showProgress(String hint) {
		mProgressDialog = JinCaiZiProgressDialog.show(this, hint);
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

	private void _getUserId() {
		sp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		int userid = sp.getInt("userid", 0);
		mUpk = sp.getString("upk", "");
		if (userid == 0) {
			mUserId = "";
		} else {
			mUserId = String.valueOf(userid);
		}
		mUserName = sp.getString("username", "");
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case LOGINREQUESTCODE:
			Log.i(TAG, "登录成功");
			mUserId = sp.getInt("userid", 0)+"";
			mUpk = sp.getString("upk", "");
			mUserName = sp.getString("username", "");
			break;
		default:
			break;
		}
	}
}
