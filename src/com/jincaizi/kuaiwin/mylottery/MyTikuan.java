package com.jincaizi.kuaiwin.mylottery;

import java.io.IOException;
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
import android.widget.LinearLayout;
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
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

public class MyTikuan extends Activity implements OnClickListener{

	private static final int LOGINREQUESTCODE = 4;
	private static final String TAG = "MyTikuan";
	private SharedPreferences sp;
	private int userid;
	private String mUpk;
	private ImageView mLeftMenu;
	private TextView mCardKind;
	private Button mSubmitTikuan;
	private boolean mHasPopwindowCreated = false;
	private PopupWindow mPopWindow;
	private LinearLayout mCardKindLayout;
	private RequestHandle myRequestHandle;
	private EditText mTikuanNum;
	private EditText mCardName;
	private EditText mCarNumber;
	private JinCaiZiProgressDialog mProgressDialog;
	private String availableAmount = "0";
	private TextView mTikuanLegal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytikuan_layout);
		sp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		userid = sp.getInt("userid", 0);
		mUpk = sp.getString("upk", "");
		_findViews();
		_setListener();
		_getUserInfo();
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
			_getUserInfo();
			break;
		default:
			break;
		}
	}

	private void _findViews() {
		findViewById(R.id.right_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		((TextView)findViewById(R.id.current_lottery)).setText("提    款");
		mLeftMenu = (ImageView)findViewById(R.id.touzhu_leftmenu);
		mCardKind = (TextView)findViewById(R.id.card_kind);
		mSubmitTikuan = (Button)findViewById(R.id.submit_tikuan);
		mCardKindLayout = (LinearLayout)findViewById(R.id.card_kind_layout);
		
		mTikuanNum = (EditText)findViewById(R.id.tikuan_num);
		mCardName = (EditText)findViewById(R.id.card_name);
		mCarNumber = (EditText)findViewById(R.id.card_content);
		
		mTikuanLegal = (TextView)findViewById(R.id.tikuan_legal);
	}
	private void _setListener() {
		mLeftMenu.setOnClickListener(this);
		mCardKindLayout.setOnClickListener(this);
		mSubmitTikuan.setOnClickListener(this);
		mTikuanLegal.setOnClickListener(this);
		mTikuanLegal.setEnabled(false);
		
		mCarNumber.addTextChangedListener(new MyCardNumWatcher());
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.tikuan_legal:
			_getUserInfo();
			break;
		case R.id.touzhu_leftmenu:
			finish();
			break;
		case R.id.submit_tikuan:
			
			try {
				if(TextUtils.isEmpty(mTikuanNum.getText()) || Float.valueOf(mTikuanNum.getText().toString()) == 0) {
					Toast.makeText(this, "请输入提款金额", Toast.LENGTH_SHORT).show();
					break;
				}
				if (Float.parseFloat(mTikuanNum.getText().toString()) > Float.parseFloat(availableAmount)) {
					Toast.makeText(this, "您的最大提款金额为"+availableAmount+"元！", Toast.LENGTH_SHORT).show();
					break;
				}
			} catch (Exception e) {
				Toast.makeText(MyTikuan.this, "请检查输入格式是否正确",
						Toast.LENGTH_SHORT).show();
                break;
			}
//			if(mTikuanNum.getText().toString().compareTo(availableAmount) > 0) {
//				Toast.makeText(this, "您的最大提款金额为"+availableAmount+"元！", Toast.LENGTH_SHORT).show();
//				break;
//			}
			
			if(TextUtils.isEmpty(mCardKind.getText())) {
				Toast.makeText(this, "请选择收款银行", Toast.LENGTH_SHORT).show();
				break;
			}
			if(!mCardKind.getText().equals("支付宝") && TextUtils.isEmpty(mCardName.getText())) {
				Toast.makeText(this, "请输入开户行名称", Toast.LENGTH_SHORT).show();
				break;
			}
			if(TextUtils.isEmpty(mCarNumber.getText())) {
				Toast.makeText(this, "请输入银行卡号", Toast.LENGTH_SHORT).show();
				break;
			}
			_postTikuanFormData();
			break;
		case R.id.card_kind_layout:
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
	                mCardKind.setText(tikuan_card_kind[arg2]);
	                mPopWindow.dismiss();
	            }
	        });
	        mPopWindow.setFocusable(true);
	        mPopWindow.setOutsideTouchable(true);
	        mPopWindow.update();
	        mPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), Bitmap.createBitmap(
	                1, 1, Config.ARGB_8888)));
	    }
	 
	 private  void _getUserInfo() {
		    _showTouzhuProgress("获取提款信息中...");
		    mTikuanLegal.setText("正在获取提款信息！");
			RequestParams params = new RequestParams();
		    params.add("act", "cashinfo");
		    params.add("userid", userid + "");
		    params.add("upk", mUpk);
			params.add("datatype", "json");
			params.add("jsoncallback",
					"jsonp" + String.valueOf(System.currentTimeMillis()));
			params.add("_", String.valueOf(System.currentTimeMillis()));
			String url = AsyncHttpClient.getUrlWithQueryString(false,
					JinCaiZiHttpClient.BASE_URL, params);
			myRequestHandle = JinCaiZiHttpClient.post(this, url,
					new AsyncHttpResponseHandler() {
						

						
						private String bank;
						private String bankCard;
						private String openSpace;
						private String responseMsg = "";

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] responseBody) {
							int result = -1;
							JsonReader reader = null;
							try {
								String charset;
								if(Utils.isCmwapNet(MyTikuan.this)) {
									charset = "utf-8";
								} else {
									charset = "gb2312";
								}
								String jsonData = new String(responseBody, charset);
								Log.d(TAG, "tikuan userinfo detail = " + jsonData);
								reader = new JsonReader(new StringReader(jsonData));
	                            reader.beginObject();
	                            while(reader.hasNext()) {
	                            	String tagName = reader.nextName();
	                            	if(tagName.equals("result")) {
	                            		result = reader.nextInt();
	                            	} else if(tagName.equals("AvailableAmount")) {
	                            		availableAmount = reader.nextString();
	                            	} else if(tagName.equals("Bank")) {
	                            		bank = reader.nextString();
	                            	} else if(tagName.equals("BankCard")) {
	                            		bankCard = reader.nextString();
	                            	} else if(tagName.equals("OpenSpace")) {
	                            		openSpace = reader.nextString();
	                            	} else if(tagName.equals("msg")) {
	                            		responseMsg = reader.nextString();
	                            	}
	                            }
	                            reader.endObject();
	                            if(result == 0) {
	                            mTikuanLegal.setText("可提款金额：" + availableAmount + "元");
	                            mCardKind.setText(bank);
	                            mCardName.setText(openSpace);
	                            mCarNumber.setText(bankCard);
	                            mTikuanLegal.setEnabled(false);
	                            } else {
	                            	if(!TextUtils.isEmpty(responseMsg) && responseMsg.equals("未登录或登录过期！")) {
										 CheckLogin.clearLoginStatus(sp);
										 Intent loginIntent = new Intent(MyTikuan.this, Login.class);
										 startActivityForResult(loginIntent, LOGINREQUESTCODE);
									 }
	                            	mTikuanLegal.setText("获取提款信息失败，点击重新获取！");
	                            	mTikuanLegal.setEnabled(true);
	                            }
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
								mTikuanLegal.setText("获取提款信息失败，点击重新获取！");
								mTikuanLegal.setEnabled(true);
							} catch (Exception e) {
								e.printStackTrace();
								mTikuanLegal.setText("获取提款信息失败，点击重新获取！");
								mTikuanLegal.setEnabled(true);
							} finally {
								_hideTouzhuProgress();
								
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
							_hideTouzhuProgress();
							Toast.makeText(MyTikuan.this, "获取失败", Toast.LENGTH_SHORT).show();
							mTikuanLegal.setText("获取提款信息失败，点击重新获取！");
							mTikuanLegal.setEnabled(true);
						}
					});
		}
	 
		private void _postTikuanFormData() {
			RequestParams rawParams = new RequestParams();
			
			rawParams.add("Bank", mCardKind.getText().toString());//提款银行
			rawParams.add("OpenSpace", mCardName.getText().toString());//开户行
			rawParams.add("BankCard", mCarNumber.getText().toString());//卡号
			rawParams.add("amount", mTikuanNum.getText().toString());//金额

			RequestParams paramsUrl = new RequestParams();
			paramsUrl.add("act", "cash");
			paramsUrl.add("userid", userid + "");
			paramsUrl.add("upk", mUpk);
			paramsUrl.add("datatype", "json");
			paramsUrl.add("jsoncallback",
					"jsonp" + String.valueOf(System.currentTimeMillis()));
			paramsUrl.add("_", String.valueOf(System.currentTimeMillis()));
			String url = AsyncHttpClient.getUrlWithQueryString(false,
					JinCaiZiHttpClient.BASE_URL, paramsUrl);

			_doHttpFormRequest(url, rawParams);

		}
		private void _doHttpFormRequest(String url,RequestParams requestparams) {
		      _showTouzhuProgress("正在提交，请等待");
		 	  JinCaiZiHttpClient.postFormData(this, url, requestparams, new AsyncHttpResponseHandler() {
				private String responseMsg = "提款申请失败！";
				private int result = -1;

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					JsonReader reader = null;
					try {
						String charset;
						if(Utils.isCmwapNet(MyTikuan.this)) {
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
                        	} 
                        }
                        reader.endObject();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						Toast.makeText(MyTikuan.this, "数据可能异常，请检查数据请求是否成功，以免重复请求", Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(MyTikuan.this, "数据可能异常，请检查数据请求是否成功，以免重复请求", Toast.LENGTH_LONG).show();
					} finally {
						// FIXME reader.close should be here
						final Dialog localDialog = new Dialog(MyTikuan.this, R.style.Theme_dialog);
					View view = LayoutInflater.from(getApplicationContext()).inflate(
								R.layout.dialog_submit_bet, null);
						TextView dialogTitle = (TextView) view
								.findViewById(R.id.submit_dialog_title);
						dialogTitle.setText("提示");
						TextView dialogContent = (TextView) view
								.findViewById(R.id.submit_dialog_content);
						TextView dialogCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
						dialogCancel.setVisibility(View.GONE);
						////view.findViewById(R.id.dialog_divider).setVisibility(View.GONE);
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
									 Intent loginIntent = new Intent(MyTikuan.this, Login.class);
									 startActivityForResult(loginIntent, LOGINREQUESTCODE);
								 }
								
							}
						});
						dialogContent.setText(responseMsg);
						localDialog.show();
					}
					_hideTouzhuProgress();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					//Log.d(TAG, "failure = " + error.toString());
					_hideTouzhuProgress();
					responseMsg = "提款失败， 请重试！";
					Toast.makeText(MyTikuan.this, responseMsg, Toast.LENGTH_SHORT).show();
					//finish();
				}
			});  
	   }
	protected void _showTouzhuProgress(String text) {
		mProgressDialog = JinCaiZiProgressDialog.show(this, text);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				try {
					Log.d(TAG, "主动取消");
					if (myRequestHandle != null) {
						myRequestHandle.cancel(true);
					}
					//Toast.makeText(MyTikuan.this, "您取消", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Log.e(TAG, "无效返回", e);
				} finally {
				}
			}
		});
	}

	private void _hideTouzhuProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
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
				String editable = mCarNumber.getText().toString();   
		        String str = stringFilter(editable.toString()); 
				//if(isChinese(s.))
		        if(!editable.equals(str)){ 
		        	mCarNumber.setText(str);
		        	Toast.makeText(MyTikuan.this, "请输入银行卡号或支付宝账号，不支持中文字符", Toast.LENGTH_SHORT).show();
		            //设置新的光标所在位置 
		        	mCarNumber.setSelection(str.length()); 
		        } 

			}
	    	
	    }
	    public static String stringFilter(String str)throws PatternSyntaxException{      
	    	Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
	        Matcher m = p.matcher(str);
            return m.replaceAll("").trim();

	    }

}
