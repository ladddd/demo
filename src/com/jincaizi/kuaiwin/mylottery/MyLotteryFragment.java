package com.jincaizi.kuaiwin.mylottery;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.FragmentCallbacks;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.R;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;

public class MyLotteryFragment extends Fragment implements OnClickListener, FragmentCallbacks{
	public static final String TAG = "MyLotteryFragment";
	public static final int loginRequestCode = 1;
	private TextView mTvLogin;
	private TextView modifyPwd;
	private SharedPreferences sp;
	private TextView loginHint;
	private ImageView headbarRightDivider;
	private TextView myHemai;
	private TextView myDaigou, myZhuihao;
	private TextView mAccountDetail;
	private int userid;
	private TextView mChongzhiView;
	private TextView mTikuanView;
	private TextView mCardPay;
	private String mUpk;
	private TextView mTotalAmount;
	private TextView mAvaliableAmount;
	private TextView mFreezAmount;
	private TextView mMoneyBrief;
	private TextView mMoneyGetFailure;
	private TextView mIdAuth;


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.mylottery_layout, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		_findViews(view);
		_setListener();
		_getLoginStatus();
		_requestData();
	}
	private void _findViews(View view) {
		mTvLogin = (TextView)view.findViewById(R.id.sumbit_group_buy);
		mTvLogin.setText("登陆");
		TextView title = (TextView)view.findViewById(R.id.current_lottery);
		  title.setText("我的彩票");
		modifyPwd = (TextView)view.findViewById(R.id.mylottory_modify_pwd);
		loginHint = (TextView)view.findViewById(R.id.login_hint_view);
		headbarRightDivider = (ImageView)view.findViewById(R.id.right_divider);
		myHemai = (TextView)view.findViewById(R.id.mylottory_hemai_record);
		myDaigou = (TextView)view.findViewById(R.id.mylottory_daigou_record);
		myZhuihao = (TextView)view.findViewById(R.id.mylottory_zhuihao_record);
		view.findViewById(R.id.touzhu_leftmenu).setVisibility(View.GONE);
		view.findViewById(R.id.left_divider).setVisibility(View.GONE);
		mAccountDetail = (TextView)view.findViewById(R.id.mylottory_mingxi_record);
		
		mChongzhiView = (TextView)view.findViewById(R.id.mylottory_chongzhi);
		mTikuanView = (TextView)view.findViewById(R.id.mylottory_tikuan);
		mCardPay = (TextView)view.findViewById(R.id.mylottory_bangding_card);
		mIdAuth = (TextView)view.findViewById(R.id.mylottory_id_auth);
		
		mTotalAmount = (TextView)view.findViewById(R.id.money_all_left);
		mAvaliableAmount = (TextView)view.findViewById(R.id.money_in_use);
		mFreezAmount = (TextView)view.findViewById(R.id.money_in_freez);
		mMoneyBrief = (TextView)view.findViewById(R.id.money_brief);
		
		mMoneyGetFailure = (TextView)view.findViewById(R.id.money_get_failure);
	}
	private void _setListener() {
		mTvLogin.setOnClickListener(this);
		modifyPwd.setOnClickListener(this);
		myHemai.setOnClickListener(this);
		myDaigou.setOnClickListener(this);
		myZhuihao.setOnClickListener(this);
		mAccountDetail.setOnClickListener(this);
		
		mChongzhiView.setOnClickListener(this);
		mTikuanView.setOnClickListener(this);
		mCardPay.setOnClickListener(this);
		mIdAuth.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		_getLoginStatus();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(userid == 0) {
			 Intent loginIntent = new Intent(getActivity(), Login.class);
			 startActivityForResult(loginIntent, loginRequestCode);
			return;
		}
		switch(v.getId()) {
		case R.id.mylottory_mingxi_record:
			Intent accountIntent = new Intent(getActivity(), MyAccountDetail.class);
			startActivity(accountIntent);
			break;
		case R.id.mylottory_zhuihao_record:
			Intent zuhaoIntent = new Intent(getActivity(), MyLotteryRecordZuihao.class);
			startActivity(zuhaoIntent);
			break;
		case R.id.mylottory_daigou_record:
			Intent daigouIntent = new Intent(getActivity(), MyLotteryRecordDaigou.class);
			startActivity(daigouIntent);
			break;
		case R.id.mylottory_hemai_record:
			Intent hemaiIntent = new Intent(getActivity(), MyLotteryRecordHemai.class);
			startActivity(hemaiIntent);
			break;
		case R.id.mylottory_modify_pwd:
			Intent modifyPwdIntent = new Intent(getActivity(), ModifyPwdActivity.class);
			startActivity(modifyPwdIntent);
			break;
		case R.id.sumbit_group_buy:
			Intent loginIntent = new Intent(getActivity(), Login.class);
			startActivityForResult(loginIntent, loginRequestCode);
		break;
		case R.id.mylottory_tikuan:
			Intent tikuanIntent = new Intent(getActivity(), MyTikuan.class);
			startActivity(tikuanIntent);
			break;
		case R.id.mylottory_chongzhi:
			
			break;
		case R.id.mylottory_bangding_card:
			Intent bdIntent = new Intent(getActivity(), CardBinding.class);
			startActivity(bdIntent);
			break;
		case R.id.mylottory_id_auth:
			Intent auIntent = new Intent(getActivity(), IdAuthActivity.class);
			startActivity(auIntent);
			break;
			default:
				Toast.makeText(getActivity(), "暂未开放", Toast.LENGTH_SHORT).show();
				break;
				
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		_getLoginStatus();
		_requestData();
	}

//	@Override
//	public void onHiddenChanged(boolean hidden) {
//		// TODO Auto-generated method stub
//		super.onHiddenChanged(hidden);
//		Log.d(TAG, "hidden = " + hidden);
//		if(!hidden) {
//			_getLoginStatus();
//			_requestData();
//		}
//	}
	private void _getLoginStatus() {
		sp = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		userid = sp.getInt("userid", 0);
		mUpk = sp.getString("upk", "");
        if(userid != 0) {
        	loginHint.setText("欢迎您 " + sp.getString("username", ""));
        	loginHint.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        	mTvLogin.setText("重新登录");
        	//mTvLogin.setVisibility(View.GONE);
        	//headbarRightDivider.setVisibility(View.GONE);
        } else {
        	mTvLogin.setText("登录");
        	loginHint.setText("您还未登录，登录后可查看详细信息");
        	loginHint.setCompoundDrawablesWithIntrinsicBounds(R.drawable.verify_hint, 0, 0, 0);
        	mTvLogin.setVisibility(View.VISIBLE);
        	headbarRightDivider.setVisibility(View.VISIBLE);
        }
	}
	
	private  void _requestData() {
		if(userid == 0) {
			return;
		}
		RequestParams params = new RequestParams();
	    params.add("act", "useraccount");
	    params.add("userid", userid + "");
	    params.add("upk", mUpk);
		params.add("datatype", "json");
		params.add("jsoncallback",
				"jsonp" + String.valueOf(System.currentTimeMillis()));
		params.add("_", String.valueOf(System.currentTimeMillis()));
		String url = AsyncHttpClient.getUrlWithQueryString(false,
				JinCaiZiHttpClient.BASE_URL, params);
		JinCaiZiHttpClient.post(getActivity(), url,
				new AsyncHttpResponseHandler() {
					

					private String totalAmount;
					private String availableAmount;
					private String freezeAmount;
					private String resMsg = "数据获取失败";

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						int result = -1;
						JsonReader reader = null;
						try {
							String charset;
							if(Utils.isCmwapNet(getActivity())) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "账户余额 = " + jsonData);
							reader = new JsonReader(new StringReader(jsonData));
                            reader.beginObject();
                            while(reader.hasNext()) {
                            	String tagName = reader.nextName();
                            	if(tagName.equals("msg")) {
                            		resMsg = reader.nextString();
                            	} else if(tagName.equals("result")) {
                            		result = reader.nextInt();
                            	} else if(tagName.equals("TotalAmount")) {
                            		totalAmount = reader.nextString();
                            	} else if(tagName.equals("AvailableAmount")) {
                            		availableAmount = reader.nextString();
                            	} else if(tagName.equals("FreezeAmount")) {
                            		freezeAmount = reader.nextString();
                            	}
                            }
                            reader.endObject();
                           
                             if(result == 0) {
                            	 mMoneyBrief.setVisibility(View.VISIBLE);
                            	 mMoneyGetFailure.setVisibility(View.GONE);
                                SpannableString ss = new SpannableString("当前余额：" + totalAmount+ "元");	
        						ss.setSpan(new ForegroundColorSpan(Color.RED), 5, ss.length()-1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        						mTotalAmount.setText(ss);
        						mTotalAmount.setVisibility(View.VISIBLE);
        						ss = new SpannableString("可用余额：" + availableAmount+ "元");	
        						ss.setSpan(new ForegroundColorSpan(Color.RED), 5, ss.length()-1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        						mAvaliableAmount.setText(ss);
        						mAvaliableAmount.setVisibility(View.VISIBLE);
        						ss = new SpannableString("冻结金额：" + freezeAmount+ "元");	
        						ss.setSpan(new ForegroundColorSpan(Color.RED), 5, ss.length()-1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        						mFreezAmount.setText(ss);
        						mFreezAmount.setVisibility(View.VISIBLE);
                            } else {
                            	_setMoneyGetResultValue(resMsg);
                            }
        					
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							resMsg = "数据获取失败";
							_setMoneyGetResultValue(resMsg);
						} catch (Exception e) {
							e.printStackTrace();
							resMsg = "数据获取失败";
							_setMoneyGetResultValue(resMsg);
							
						} finally {
							
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
						resMsg = "数据获取失败";
						_setMoneyGetResultValue(resMsg);
					}
				});
	}
	private void _setMoneyGetResultValue(String resMsg) {
		mMoneyBrief.setVisibility(View.VISIBLE);
		mMoneyGetFailure.setVisibility(View.VISIBLE);
    	mMoneyGetFailure.setText(resMsg);
    	mTotalAmount.setVisibility(View.GONE);
    	mAvaliableAmount.setVisibility(View.GONE);
    	mFreezAmount.setVisibility(View.GONE);
	}

	@Override
	public void onShow() {
		Log.i(TAG, "onshow in mylotteryfragment");
		_getLoginStatus();
		_requestData();
		
	}
}
