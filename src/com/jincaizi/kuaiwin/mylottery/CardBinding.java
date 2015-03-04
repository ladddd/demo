package com.jincaizi.kuaiwin.mylottery;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.jincaizi.vendor.http.RequestParams;

public class CardBinding extends Activity implements OnClickListener{
	private static final int LOGINREQUESTCODE = 4;
	private static final String TAG = "CardBinding";
	private TextView mGetUserInfoView;
	private TextView mUserRealNameView;
	private TextView mUserCardIdView;
	private TextView mCardTypeView;
	private EditText mOpenSpaceView;
	private EditText mCardNumView;
	private SharedPreferences sp;
	private int userid;
	private String mUpk;
	private ImageView mLeftMenu;
	private PopupWindow mPopWindow;
	private boolean mHasPopwindowCreated = false;
	private Button mBtnBind;
	private Button mBtnUnbind;
	private JinCaiZiProgressDialog mProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardbinding);
		sp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		userid = sp.getInt("userid", 0);
		mUpk = sp.getString("upk", "");
		_findViews();
		_setListener();
		_showTouzhuProgress("正在获取用户信息，请等待");
		_postBindFormData(3);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case LOGINREQUESTCODE:
			Log.i(TAG, "登录成功");
			userid = sp.getInt("userid", 0);
			mUpk = sp.getString("upk", "");
			// _postFormData();
			break;
		default:
			break;
		}
	}
    private void _findViews() {
    	findViewById(R.id.right_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		((TextView)findViewById(R.id.current_lottery)).setText("银行卡绑定");
		mLeftMenu = (ImageView)findViewById(R.id.touzhu_leftmenu);
    	
    	mGetUserInfoView = (TextView)findViewById(R.id.getuserinfo_hint);
    	mUserRealNameView = (TextView)findViewById(R.id.user_realname);
    	mUserCardIdView = (TextView)findViewById(R.id.user_cardID);
    	mCardTypeView = (TextView)findViewById(R.id.card_type);
    	mOpenSpaceView = (EditText)findViewById(R.id.card_openspace);
    	mCardNumView = (EditText)findViewById(R.id.card_num);
    	
    	mBtnBind = (Button)findViewById(R.id.btn_bind);
    	mBtnUnbind = (Button)findViewById(R.id.btn_unbind);
    	mBtnBind.setEnabled(false);
    	mBtnUnbind.setEnabled(false);
    }
    private void _setListener() {
    	mLeftMenu.setOnClickListener(this);
    	mGetUserInfoView.setOnClickListener(this);
    	mBtnBind.setOnClickListener(this);
    	mBtnUnbind.setOnClickListener(this);
    	findViewById(R.id.card_type_layout).setOnClickListener(this);
    	mCardNumView.addTextChangedListener(new MyCardNumWatcher());
    }
   
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.touzhu_leftmenu:
			finish();
			break;
		case R.id.getuserinfo_hint:
			_showTouzhuProgress("正在获取用户信息，请等待");
			_postBindFormData(3);
			break;
		case R.id.btn_bind:
			if(TextUtils.isEmpty(mCardTypeView.getText())) {
				Toast.makeText(this, "请选择收款银行", Toast.LENGTH_SHORT).show();
				break;
			}
			if(!mCardTypeView.getText().equals("支付宝") && TextUtils.isEmpty(mOpenSpaceView.getText())) {
				Toast.makeText(this, "请输入开户行名称", Toast.LENGTH_SHORT).show();
				break;
			}
			if(TextUtils.isEmpty(mCardNumView.getText())) {
				Toast.makeText(this, "请输入银行卡号", Toast.LENGTH_SHORT).show();
				break;
			}
			_showTouzhuProgress("正在提交，请等待");
			_postBindFormData(1);
			break;
		case R.id.btn_unbind:
			_showTouzhuProgress("正在提交，请等待");
			_postBindFormData(2);
			break;
		case R.id.card_type_layout:
			 if (!mHasPopwindowCreated) {
                _setPopWindow(v.getWidth());
                mHasPopwindowCreated = true;
            }
            mPopWindow.showAsDropDown(v);
			break;
			default:
				break;
		}
	}
	
	 private void _setPopWindow(int width) {
	        View view = LayoutInflater.from(this).inflate(R.layout.tikuan_popview, null);
	        mPopWindow = new PopupWindow(view, width, LayoutParams.WRAP_CONTENT);
	        GridView mListView = (GridView) view.findViewById(R.id.pop_listview);
	        mListView.setBackgroundColor(this.getResources().getColor(R.color.white));
	        final String[]tikuan_card_kind = this.getResources().getStringArray(R.array.tikuan_card_kind);
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	                R.layout.tikuan_popview_item, tikuan_card_kind);
	        mListView.setAdapter(adapter);
	        mListView.setOnItemClickListener(new OnItemClickListener() {

	            @Override
	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	                mCardTypeView.setText(tikuan_card_kind[arg2]);
	                mPopWindow.dismiss();
	            }
	        });
	        mPopWindow.setFocusable(true);
	        mPopWindow.setOutsideTouchable(true);
	        mPopWindow.update();
	        mPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), Bitmap.createBitmap(
	                1, 1, Config.ARGB_8888)));
	    }
	   class MyCardNumWatcher implements TextWatcher {

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
				// TODO Auto-generated method stub
				String editable = mCardNumView.getText().toString();   
		        String str = stringFilter(editable.toString()); 
				//if(isChinese(s.))
		        if(!editable.equals(str)){ 
		        	mCardNumView.setText(str);
		        	Toast.makeText(CardBinding.this, "请输入银行卡号或支付宝账号，不支持中文字符", Toast.LENGTH_SHORT).show();
		            //设置新的光标所在位置 
		        	mCardNumView.setSelection(str.length()); 
		        } 

			}
	    	
	    }
	    public static String stringFilter(String str)throws PatternSyntaxException{      
	    	Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
	        Matcher m = p.matcher(str);
         return m.replaceAll("").trim();

	    }
		private void _postBindFormData(int binding) {
			int flag = binding;
			RequestParams rawParams = new RequestParams();
			if(binding == 1) {
				rawParams.add("flag", "1");//提款银行
				rawParams.add("bank", mCardTypeView.getText().toString());//卡种
				rawParams.add("OpenSpace", mOpenSpaceView.getText().toString());//开户行
				rawParams.add("BankCard", mCardNumView.getText().toString());//卡号
			} else if(binding ==2){
				rawParams.add("flag","2");//提款银行
				rawParams.add("bank", "");//金额
				rawParams.add("OpenSpace", "");//开户行
				rawParams.add("BankCard", "");//卡号
			} else {
				rawParams.add("flag","3");//提款银行
				rawParams.add("bank", "");//金额
				rawParams.add("OpenSpace", "");//开户行
				rawParams.add("BankCard", "");//卡号
			}
			
			

			RequestParams paramsUrl = new RequestParams();
			paramsUrl.add("act", "userbankbind");
			paramsUrl.add("userid", userid + "");
			paramsUrl.add("upk", mUpk);
			paramsUrl.add("datatype", "json");
			paramsUrl.add("jsoncallback",
					"jsonp" + String.valueOf(System.currentTimeMillis()));
			paramsUrl.add("_", String.valueOf(System.currentTimeMillis()));
			String url = AsyncHttpClient.getUrlWithQueryString(false,
					JinCaiZiHttpClient.BASE_URL, paramsUrl);

			_doHttpFormRequest(url, rawParams, flag);

		}
		private void _doHttpFormRequest(String url,RequestParams requestparams, final int flag) {
		      
		 	  JinCaiZiHttpClient.postFormData(this, url, requestparams, new AsyncHttpResponseHandler() {
				private String responseMsg = "处理失败！";
				private int result = -1;

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					JsonReader reader = null;
					try {
						String charset;
						if(Utils.isCmwapNet(CardBinding.this)) {
							charset = "utf-8";
						} else {
							charset = "gb2312";
						}
						String jsonData = new String(responseBody, charset);
						Log.d(TAG, "tikuan shenqing  form data = " + jsonData);
						reader = new JsonReader(new StringReader(jsonData));
                        reader.beginObject();
                        while(reader.hasNext()) {
                        	String tagName = reader.nextName();
                        	if(tagName.equals("result")) {
                        		result  = reader.nextInt();
                        	} else if(tagName.equals("msg")) {
                        		responseMsg = reader.nextString();
                        	}  else if(tagName.equals("RealName")) {
                        		mGetUserInfoView.setVisibility(View.INVISIBLE);
                        		String realName = reader.nextString();
                        		if(TextUtils.isEmpty(realName)) {
                        			mUserRealNameView.setText("您还未实名认证");
                        		} else {
                        		    mUserRealNameView.setText(realName);
                        		}
                        	} else if(tagName.equals("IdNumber")) {
                        		String idNumber = reader.nextString();
                        		if(TextUtils.isEmpty(idNumber)) {
                        			mUserCardIdView.setText("您还未实名认证");
                        		} else {
                        			mUserCardIdView.setText(idNumber);
                        		}
                        		
                        	} else if(tagName.equals("Bank")) {
                        		mCardTypeView.setText(reader.nextString());
                        	}else if(tagName.equals("BankCard")) {
                        		mCardNumView.setText(reader.nextString());
                        	}else if(tagName.equals("OpenSpace")) {
                        		mOpenSpaceView.setText(reader.nextString());
                        	}
                        }
                        reader.endObject();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						responseMsg = "操作失败！";
					} catch (Exception e) {
						e.printStackTrace();
						responseMsg = "操作失败！";
					} finally {
						if(flag == 3) {
							if(result == 0) {
							_hideTouzhuProgress();
							mBtnBind.setEnabled(true);
							mBtnUnbind.setEnabled(true);
							return;
							} else {
								mGetUserInfoView.setText("点击重新获取用户信息");
							}
						}
						_showHintDialog(result, responseMsg);
					    _hideTouzhuProgress();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					//Log.d(TAG, "failure = " + error.toString());
					_hideTouzhuProgress();
					responseMsg = "操作失败， 请重试！";
					Toast.makeText(CardBinding.this, responseMsg, Toast.LENGTH_SHORT).show();
					if(flag == 3) {
						mGetUserInfoView.setText("点击重新获取用户信息");
					}
				}
			});  
	   }
		protected void _showTouzhuProgress(String text) {
			mProgressDialog = JinCaiZiProgressDialog.show(this, text);
			
		}

		private void _hideTouzhuProgress() {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
		}
		private void _showHintDialog(final int result, final String responseMsg) {
			final Dialog localDialog = new Dialog(CardBinding.this, R.style.Theme_dialog);
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
						 if(result == 0) {
							 finish();
						 } else if(!TextUtils.isEmpty(responseMsg) && responseMsg.equals("未登录或登录过期！")) {
							 CheckLogin.clearLoginStatus(sp);
							 Intent loginIntent = new Intent(CardBinding.this, Login.class);
							 startActivityForResult(loginIntent, LOGINREQUESTCODE);
						 }
						
					}
				});
				dialogContent.setText(responseMsg);
				localDialog.show();
			}
}
